package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.repositories.ConnectionsRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.Set;
@Service
public class ConnectServiceImpl implements ConnectService {

    @Autowired
    CacheService cache;

    @Autowired
    ConnectionsRepository connectionsRepository;

    private static final Log logger = LogFactory.getLog(ConnectServiceImpl.class);

    private int qrCodeStringLength = 12;

    public Optional<String> getQRCodeString(long userId){
        String userIdToCacheKey = String.valueOf(userId);
        String getCode = cache.get("ConnectQRCodeString", userIdToCacheKey);
        Optional<String> opt = Optional.of(getCode);
        return opt;

    }

    public void storeQRCodeString(long userId){
        String generatedQRCodeString = generateRandomString(qrCodeStringLength);
        String userIdToCacheKey = String.valueOf(userId);
        cache.put("ConnectQRCodeString", userIdToCacheKey, generatedQRCodeString);
        logger.debug("User ID: " + userId + " ConnectQRCodeString: " + generatedQRCodeString);

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

    public boolean connect(Long requestingUserId, Long toBeConnectedWithUserId, String qrcodePhrase) {

        Optional<Connection> opt = Optional.of(connectionsRepository.save(new Connection(requestingUserId, toBeConnectedWithUserId)));

        if(opt.isPresent()) {
            return true;
        }
        return false;
    }
}
