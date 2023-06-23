package com.savvato.tribeapp.services;

import com.savvato.tribeapp.config.principal.UserPrincipal;
import com.savvato.tribeapp.dto.ConnectIncomingMessageDTO;
import org.springframework.messaging.handler.annotation.Header;

import java.util.Optional;

public interface ConnectService {

    Optional<String> getQRCodeString(long userId);

    Optional<String> storeQRCodeString(long userId);
    void connect(ConnectIncomingMessageDTO incoming, UserPrincipal user);
}
