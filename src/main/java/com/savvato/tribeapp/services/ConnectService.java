package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.ConnectionRemovalRequest;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTOUpdated;

import java.util.List;
import java.util.Optional;

public interface ConnectService {

    // deprecated
    List<ConnectOutgoingMessageDTO> getAllConnectionsForAUser(Long userId);

    Optional<String> getQRCodeString(long userId);

    Optional<String> storeQRCodeString(long userId);

    Boolean validateQRCode(String qrcodePhrase, Long toBeConnectedWithUserId);
    void connect(ConnectIncomingMessageDTO incoming);

    boolean saveConnectionDetails(Long requestingUserId, Long toBeConnectedWithUserId);
    ConnectOutgoingMessageDTO handleConnectionIntent(String connectionIntent, Long requestingUserId, Long toBeRequestedWithUserId);

    List<ConnectOutgoingMessageDTOUpdated> getAllConnectionsForAUserUpdated(Long userId);

    boolean removeConnection(ConnectionRemovalRequest connectionDeleteRequest);
}
