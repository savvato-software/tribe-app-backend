package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.ConnectionRemovalRequest;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.util.List;
import java.util.Optional;

public interface ConnectService {

    List<ConnectOutgoingMessageDTO> getAllConnectionsForAUser(Long userId);

    Optional<String> getQRCodeString(long userId);

    Optional<String> storeQRCodeString(long userId);

    Boolean validateQRCode(String qrcodePhrase, Long toBeConnectedWithUserId);
//    void connect(ConnectIncomingMessageDTO incoming);

    boolean saveConnectionDetails(Long requestingUserId, Long toBeConnectedWithUserId);

    @MessageMapping("/connect/room")
    void connect(ConnectIncomingMessageDTO incoming);

    List<ConnectOutgoingMessageDTO> handleConnectionIntent(String connectionIntent, Long requestingUserId, Long toBeConnectedWithUserId);

    boolean removeConnection(ConnectionRemovalRequest connectionDeleteRequest);
}
