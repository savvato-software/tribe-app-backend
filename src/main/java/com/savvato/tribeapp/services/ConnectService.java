package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Connection;

import java.util.List;
import java.util.Optional;

public interface ConnectService {

    Optional<String> getQRCodeString(long userId);

    Optional<String> storeQRCodeString(long userId);
    boolean connect(Long requestingUserId, Long toBeConnectedWithUserId, String qrcodePhrase);

    Optional<String> getAllUserConnection(long userId, long userIdToBeConnected);

    public List<Connection> getUserId2(Long id2);

}
