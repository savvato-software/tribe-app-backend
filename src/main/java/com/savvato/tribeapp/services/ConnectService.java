package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.ConnectionRemovalRequest;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;

import java.util.Optional;

public interface ConnectService {

    Optional<String> getQRCodeString(long userId);

    Optional<String> storeQRCodeString(long userId);

    Boolean validateQRCode(String qrcodePhrase, Long toBeConnectedWithUserId);

    void connect(ConnectIncomingMessageDTO incoming);

    boolean saveConnectionDetails(Long requestingUserId, Long toBeConnectedWithUserId);

    ConnectOutgoingMessageDTO handleConnectionIntent(String connectionIntent, Long requestingUserId, Long toBeRequestedWithUserId);

    boolean removeConnection(ConnectionRemovalRequest connectionDeleteRequest);
}
