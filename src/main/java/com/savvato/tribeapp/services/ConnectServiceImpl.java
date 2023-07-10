package com.savvato.tribeapp.services;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.controllers.dto.ConnectRequest;
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

    public Optional<String> getQRCodeString(long userId){
        String userIdToCacheKey = String.valueOf(userId);
        String getCode = cache.get("ConnectQRCodeString", userIdToCacheKey);
        Optional<String> opt = Optional.of(getCode);
        return opt;

    }

    public Optional<String> storeQRCodeString(long userId){
        String generatedQRCodeString = generateRandomString(QRCODE_STRING_LENGTH);
        String userIdToCacheKey = String.valueOf(userId);
        cache.put("ConnectQRCodeString", userIdToCacheKey, generatedQRCodeString);
        logger.debug("User ID: " + userId + " ConnectQRCodeString: " + generatedQRCodeString);
        return Optional.of(generatedQRCodeString);
    }

    private String generateRandomString(int length){
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return new String(digits);
    }

    private boolean saveConnectionDetails(Long requestingUserId, Long toBeConnectedWithUserId) {
        Optional<Connection> opt = Optional.of(connectionsRepository.save(new Connection(requestingUserId, toBeConnectedWithUserId)));

        if (opt.isPresent()) {
            return true;
        }
        return false;
    }

    private Boolean validateQRCode(String qrcodePhrase, Long toBeConnectedWithUserId) {
        return qrcodePhrase.equals(getQRCodeString(toBeConnectedWithUserId).get());
    }

    public void connect(ConnectIncomingMessageDTO incoming, UserPrincipal user) {
        if(!validateQRCode(incoming.qrcodePhrase, incoming.toBeConnectedWithUserId)) {
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
            List<Long> recipients = new ArrayList<>(Arrays.asList(toBeConnectedWithUserId));
            return ConnectOutgoingMessageDTO.builder().message("Please confirm that you wish to connect.").to(recipients).build();
        } else {
            if (connectionIntent == "confirmed") {
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
        }
        return null;
    }
}
