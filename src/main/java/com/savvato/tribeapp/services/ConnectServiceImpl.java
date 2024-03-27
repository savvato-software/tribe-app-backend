package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.ConnectionRemovalRequest;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import com.savvato.tribeapp.dto.UsernameDTO;
import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.repositories.ConnectionsRepository;
import com.savvato.tribeapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ConnectServiceImpl implements ConnectService {

    @Autowired
    CacheService cache;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    ConnectionsRepository connectionsRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private final int QRCODE_STRING_LENGTH = 12;

    public Optional<String> getQRCodeString(long userId) {
        String userIdToCacheKey = String.valueOf(userId);
        String getCode = cache.get("ConnectQRCodeString", userIdToCacheKey);
        Optional<String> opt = Optional.ofNullable(getCode);
        return opt;
    }

    public Optional<String> storeQRCodeString(long userId) {
        String generatedQRCodeString = generateRandomString(QRCODE_STRING_LENGTH);
        String userIdToCacheKey = String.valueOf(userId);
        cache.put("ConnectQRCodeString", userIdToCacheKey, generatedQRCodeString);
        log.debug("User ID: " + userId + " ConnectQRCodeString: " + generatedQRCodeString);
        return Optional.of(generatedQRCodeString);
    }

    private String generateRandomString(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return new String(digits);
    }

    public boolean saveConnectionDetails(Long requestingUserId, Long toBeConnectedWithUserId) {

        try {
            connectionsRepository.save(new Connection(requestingUserId, toBeConnectedWithUserId));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Boolean validateQRCode(String qrcodePhrase, Long toBeConnectedWithUserId) {
        return qrcodePhrase.equals(getQRCodeString(toBeConnectedWithUserId).orElse("")) && StringUtils.isNotBlank(qrcodePhrase);
    }

    @Override
    @MessageMapping("/connect/room")
    public void connect(ConnectIncomingMessageDTO incoming) {
        if (!validateQRCode(incoming.qrcodePhrase, incoming.toBeConnectedWithUserId)) {
            ConnectOutgoingMessageDTO msg = ConnectOutgoingMessageDTO.builder()
                    .connectionError(true)
                    .message("Invalid QR code; failed to connect.")
                    .build();
            simpMessagingTemplate.convertAndSendToUser(
                    String.valueOf(incoming.toBeConnectedWithUserId),
                    "/connect/user/queue/specific-user",
                    msg);
        } else {
            List<ConnectOutgoingMessageDTO> outgoingMsg = handleConnectionIntent(incoming.connectionIntent, incoming.requestingUserId, incoming.toBeConnectedWithUserId);
            for (ConnectOutgoingMessageDTO dto : outgoingMsg) {
                simpMessagingTemplate.convertAndSendToUser(
                        String.valueOf(dto.to),
                        "/connect/user/queue/specific-user",
                        outgoingMsg);
            }
        }
    }

    @Override
    public List<ConnectOutgoingMessageDTO> handleConnectionIntent(String connectionIntent, Long requestingUserId, Long toBeConnectedWithUserId) {
        List<ConnectOutgoingMessageDTO> rtn = new ArrayList<>();
        List<Long> allRecipients = new ArrayList<>(Arrays.asList(requestingUserId, toBeConnectedWithUserId));

        if (connectionIntent == "") {
            rtn.add(ConnectOutgoingMessageDTO.builder()
                    .message("Please confirm that you wish to connect.")
                            .to(UsernameDTO.builder()
                                    .userId(toBeConnectedWithUserId)
                                    .username(userRepository.findById(toBeConnectedWithUserId).get().getName())
                                    .build())
                    .build());

            return rtn;
        } else if (connectionIntent == "confirmed") {
            Boolean connectionStatus = saveConnectionDetails(requestingUserId, toBeConnectedWithUserId);
            for(Long id : allRecipients) {
                if (connectionStatus) {
                    rtn.add(ConnectOutgoingMessageDTO.builder()
                            .connectionSuccess(true)
                            .to(UsernameDTO.builder()
                                    .userId(id)
                                    .username(userRepository.findById(id).get().getName())
                                    .build())
                            .message("Successfully saved connection!")
                            .build());
                } else {
                    rtn.add(ConnectOutgoingMessageDTO.builder()
                            .connectionError(true)
                            .to(UsernameDTO.builder()
                                    .userId(id)
                                    .username(userRepository.findById(id).get().getName())
                                    .build())
                            .message("Failed to save connection to database.")
                            .build());
                }
            }
        } else if (connectionIntent == "denied") {
            for(Long id : allRecipients) {
                rtn.add(ConnectOutgoingMessageDTO.builder()
                        .connectionError(true)
                        .to(UsernameDTO.builder()
                                .userId(id)
                                .username(userRepository.findById(id).get().getName())
                                .build())
                        .message("Connection request denied.")
                        .build());
            }
        } else {
            rtn = null;
        }

        return rtn;
    }

    @Override
    public List<ConnectOutgoingMessageDTO> getAllConnectionsForAUser(Long userId) {
        List<Connection> connections = connectionsRepository.findAllByToBeConnectedWithUserId(userId);
        List<ConnectOutgoingMessageDTO> outgoingMessages = new ArrayList<>();
        for (Connection connection : connections) {
            ConnectOutgoingMessageDTO outgoingMessage = ConnectOutgoingMessageDTO.builder()
                    .connectionSuccess(true)
                    .to(UsernameDTO.builder()
                            .userId(connection.getRequestingUserId())
                            .username(userRepository.findById(connection.getRequestingUserId()).get().getName())
                            .build())
                    .message("")
                    .build();
            outgoingMessages.add(outgoingMessage);
        }
        return outgoingMessages;
    }

    public boolean removeConnection(ConnectionRemovalRequest connectionRemovalRequest) {
        if (Objects.equals(connectionRemovalRequest.requestingUserId, connectionRemovalRequest.connectedWithUserId)) {
            return false;
        }
        try {
            connectionsRepository.removeConnection(connectionRemovalRequest.requestingUserId, connectionRemovalRequest.connectedWithUserId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean validateConnection(Long requestingUserId, Long toBeConnectedWithUserId) {
        Long loggedInUser = userService.getLoggedInUserId();

        if (!loggedInUser.equals(requestingUserId)) {
            log.error("The logged in user (" + loggedInUser + ") does not match issuing user (" + requestingUserId + ")");
            return false;
        }

        if (requestingUserId.equals(toBeConnectedWithUserId)) {
            log.error("User " + requestingUserId + " may not cosign themselves");
            return false;
        }

        Optional<Connection> existingConnectionWithReversedIds = connectionsRepository.findExistingConnectionWithReversedUserIds(requestingUserId, toBeConnectedWithUserId);

        if (existingConnectionWithReversedIds.isPresent()) {
            log.error("This connection already exists in reverse between the requesting user " + requestingUserId + " and the to be connected with user " + toBeConnectedWithUserId);
            return false;
        }

        return true;
    }
}
