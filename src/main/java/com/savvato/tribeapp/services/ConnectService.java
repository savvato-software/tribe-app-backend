package com.savvato.tribeapp.services;

import java.util.Optional;

public interface ConnectService {

    Optional<String> getQRCodeString(long userId);

    void storeQRCodeString(long userId);
}
