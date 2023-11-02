package com.savvato.tribeapp.services;

import com.plivo.api.models.message.MessageCreateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class SMSTextMessageServiceImplTest {

    @TestConfiguration
    static class SMSTextMessageServiceTestContextConfiguration {

        @Bean
        public SMSTextMessageService smsTextMessageService() {
            return new SMSTextMessageServiceImpl();
        }
    }

    @Autowired
    SMSTextMessageService smsTextMessageService;

    @MockBean
    CacheService cacheService;


    @Test
    public void sendSMSWhenPhoneNumberDoesntStartWithZero() {
        String phoneNumber = "1234567890";
        String msg = "A message";
        MessageCreateResponse response = new MessageCreateResponse();
        response.setMessage(msg);
        Optional<MessageCreateResponse> responseOpt = Optional.of(response);
        SMSTextMessageService smsTextMessageServiceSpy = spy(smsTextMessageService);
        doNothing().when(smsTextMessageServiceSpy).initialize();
        doReturn(responseOpt).when(smsTextMessageServiceSpy).createResponse(anyString(), anyString());
        ArgumentCaptor<String> phoneNumberCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);

        boolean rtn = smsTextMessageServiceSpy.sendSMS(phoneNumber, msg);
        verify(smsTextMessageServiceSpy, times(1)).initialize();
        verify(smsTextMessageServiceSpy, times(1)).createResponse(phoneNumberCaptor.capture(), msgCaptor.capture());
        assertEquals(phoneNumberCaptor.getValue(), phoneNumber);
        assertEquals(msgCaptor.getValue(), msg);
        assertTrue(rtn);
    }

    @Test
    public void sendSMSWhenPhoneNumberStartsWithZero() {
        String phoneNumber = "0123456789";
        String msg = "A message";
        SMSTextMessageService smsTextMessageServiceSpy = spy(smsTextMessageService);

        boolean rtn = smsTextMessageServiceSpy.sendSMS(phoneNumber, msg);
        verify(smsTextMessageServiceSpy, never()).initialize();
        verify(smsTextMessageServiceSpy, never()).createResponse(anyString(), anyString());

        assertTrue(rtn);
    }

    @Test
    public void sendSMSWhenExceptionThrown() {
        String phoneNumber = "123456789";
        String msg = "A message";
        SMSTextMessageService smsTextMessageServiceSpy = spy(smsTextMessageService);
        doThrow(IllegalArgumentException.class).when(smsTextMessageServiceSpy).initialize();
        boolean rtn = smsTextMessageServiceSpy.sendSMS(phoneNumber, msg);
        verify(smsTextMessageServiceSpy, never()).createResponse(anyString(), anyString());
        assertFalse(rtn);
    }
}
