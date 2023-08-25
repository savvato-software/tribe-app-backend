package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Connection;

import java.util.*;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import com.savvato.tribeapp.dto.ConnectOutgoingMessageDTO;
import org.springframework.messaging.handler.annotation.Header;


public interface ConnectService {

    Optional<String> getQRCodeString(long userId);

    Optional<String> storeQRCodeString(long userId);

    boolean connect(Long requestingUserId, Long toBeConnectedWithUserId, String qrcodePhrase);

    Optional<String> getAllUserConnection(long userId, long userIdToBeConnected);

    public List<Connection> getByToBeConnectedWithUserId(Long toBeConnectedWithUserId);

    Boolean validateQRCode(String qrcodePhrase, Long toBeConnectedWithUserId);
    void connect(ConnectIncomingMessageDTO incoming, UserPrincipal user);

    boolean saveConnectionDetails(Long requestingUserId, Long toBeConnectedWithUserId);
    ConnectOutgoingMessageDTO handleConnectionIntent(String connectionIntent, Long requestingUserId, Long toBeRequestedWithUserId);
}
