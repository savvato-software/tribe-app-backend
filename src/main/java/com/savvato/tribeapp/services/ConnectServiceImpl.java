package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.ConnectionRemovalRequest;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.repositories.ConnectionsRepository;
import com.savvato.tribeapp.repositories.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConnectServiceImpl implements ConnectService {

    @Autowired
    CacheService cache;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    ConnectionsRepository connectionsRepository;

    @Autowired
    UserRepository userRepository;

    private static final Log logger = LogFactory.getLog(ConnectServiceImpl.class);

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
        logger.debug("User ID: " + userId + " ConnectQRCodeString: " + generatedQRCodeString);
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
        if (requestingUserId.equals(toBeConnectedWithUserId)) {
            return false;
        }
        Optional<Connection> existingConnectionWithReversedIds = connectionsRepository.findExistingConnectionWithReversedUserIds(requestingUserId, toBeConnectedWithUserId);
        if (existingConnectionWithReversedIds.isPresent()) {
            return false;
        }
        try {
            connectionsRepository.save(new Connection(requestingUserId, toBeConnectedWithUserId));
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Boolean validateQRCode(String qrcodePhrase, Long toBeConnectedWithUserId) {
        return qrcodePhrase.equals(getQRCodeString(toBeConnectedWithUserId).orElse(""));
    }

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
            ConnectOutgoingMessageDTO outgoingMsg = handleConnectionIntent(incoming.connectionIntent, incoming.requestingUserId, incoming.toBeConnectedWithUserId);
            for (Long userId : outgoingMsg.to) {
                simpMessagingTemplate.convertAndSendToUser(
                        String.valueOf(userId),
                        "/connect/user/queue/specific-user",
                        outgoingMsg);
            }
        }
    }

    public ConnectOutgoingMessageDTO handleConnectionIntent(String connectionIntent, Long requestingUserId, Long toBeConnectedWithUserId) {
        if (connectionIntent == "") {
            List<Long> recipients = new ArrayList<>(Collections.singletonList(toBeConnectedWithUserId));
            return ConnectOutgoingMessageDTO.builder().message("Please confirm that you wish to connect.").to(recipients).build();
        } else if (connectionIntent == "confirmed") {
            Boolean connectionStatus = saveConnectionDetails(requestingUserId, toBeConnectedWithUserId);
            List<Long> recipients = new ArrayList<>(Arrays.asList(requestingUserId, toBeConnectedWithUserId));
            if (connectionStatus) {
                return ConnectOutgoingMessageDTO.builder()
                        .connectionSuccess(true)
                        .to(recipients)
                        .message("Successfully saved connection!").build();
            } else {
                return ConnectOutgoingMessageDTO.builder()
                        .connectionError(true)
                        .to(recipients)
                        .message("Failed to save connection to database.").build();
            }
        } else if (connectionIntent == "denied") {
            List<Long> recipients = new ArrayList<>(Arrays.asList(requestingUserId, toBeConnectedWithUserId));
            return ConnectOutgoingMessageDTO.builder()
                    .connectionError(true)
                    .to(recipients)
                    .message("Connection request denied.").build();

        }
        return null;
    }

    public List<ConnectOutgoingMessageDTO> getAllConnectionsForAUser(Long userId) {
        List<Connection> connections = connectionsRepository.findAllByToBeConnectedWithUserId(userId);
        List<ConnectOutgoingMessageDTO> outgoingMessages = new ArrayList<>();
        for (Connection connection : connections) {
            ConnectOutgoingMessageDTO outgoingMessage = ConnectOutgoingMessageDTO.builder()
                    .connectionSuccess(true)
                    .to(new ArrayList<>(Collections.singletonList(connection.getRequestingUserId())))
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
}
