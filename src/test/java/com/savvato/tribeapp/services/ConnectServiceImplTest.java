package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Connection;
import com.savvato.tribeapp.entities.RejectedNonEnglishWord;
import com.savvato.tribeapp.repositories.ConnectionsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class ConnectServiceImplTest {
    @TestConfiguration
    static class ConnectServiceImplTestContextConfiguration {
        @Bean
        public ConnectService connectService() {
            return new ConnectServiceImpl();
        }

        @Bean
        public CacheService cacheService() {return new CacheServiceImpl();}
    }

    @Autowired
    ConnectService connectService;

    @MockBean
    CacheService cacheService;

    @MockBean
    ConnectionsRepository connectionsRepository;

    @Test
    public void getQRCodeString() {
        Long userId = 1L;
        Optional<String> qrCodeString = Optional.of("QR code");
        Mockito.when(cacheService.get(Mockito.any(), Mockito.any())).thenReturn(qrCodeString.get());
        Optional<String> rtn = connectService.getQRCodeString(userId);
        assertEquals(qrCodeString, rtn);
    }
    @Test
    public void storeQRCodeString() {
        Long userId = 1L;
        Optional<String> generatedQRCodeString = Optional.of("QR code");

        Optional<String> rtn = connectService.storeQRCodeString(userId);

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
        verify(cacheService, times(1)).put(arg1.capture(), arg2.capture(), Mockito.any());

        assertEquals(arg1.getValue(), "ConnectQRCodeString");
        assertEquals(arg2.getValue(), String.valueOf(userId));
    }
    @Test
    public void connectWhenQrCodeIsInvalid() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String qrcodePhrase = "invalid code";

        Mockito.when(cacheService.get(Mockito.any(), Mockito.any())).thenReturn("a valid code");

        boolean rtn = connectService.connect(requestingUserId, toBeConnectedWithUserId, qrcodePhrase);
        assertEquals(rtn, false);
        verify(connectionsRepository, never()).save(Mockito.any());
    }

    @Test
    public void connectWhenQrCodeIsValid() {
        Long requestingUserId = 1L;
        Long toBeConnectedWithUserId = 2L;
        String qrcodePhrase = "a valid code";
        Connection connection = new Connection(requestingUserId, toBeConnectedWithUserId);

        Mockito.when(cacheService.get(Mockito.any(), Mockito.any())).thenReturn("a valid code");
        Mockito.when(connectionsRepository.save(Mockito.any())).thenReturn(connection);

        boolean rtn = connectService.connect(requestingUserId, toBeConnectedWithUserId, qrcodePhrase);
        assertEquals(rtn, true);

        ArgumentCaptor<Connection> arg1 = ArgumentCaptor.forClass(Connection.class);
        verify(connectionsRepository, times(1)).save(arg1.capture());
        assertEquals(arg1.getValue().getRequestingUserId(), requestingUserId);
        assertEquals(arg1.getValue().getToBeConnectedWithUserId(), toBeConnectedWithUserId);
    }

}
