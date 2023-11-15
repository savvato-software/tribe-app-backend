package com.savvato.tribeapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SMSChallengeCodeServiceImplTest extends AbstractServiceImplTest {
    @TestConfiguration
    static class SMSChallengeCodeServiceTestContextConfiguration {
        @Bean
        public SMSChallengeCodeService smsChallengeCodeService() {
            return new SMSChallengeCodeServiceImpl();
        }
    }

    @Autowired
    private SMSChallengeCodeService smsChallengeCodeService;

    @MockBean
    private CacheService cacheService;

    @MockBean
    private SMSTextMessageService smss;

    @Test
    public void sendSMSChallengeCodeToPhoneNumberHappyPath() {
        String code = "123456";
        String phoneNumber = "1234567890";
        String cacheKey = "SMSChallengeCodesByPhoneNumber";
        SMSChallengeCodeService spy = spy(smsChallengeCodeService);
        when(smss.sendSMS(anyString(), anyString())).thenReturn(true);
        ArgumentCaptor<String> cacheKeyCaptor, phoneNumberCaptor, codeCaptor;
        cacheKeyCaptor = ArgumentCaptor.forClass(String.class);
        phoneNumberCaptor = ArgumentCaptor.forClass(String.class);
        codeCaptor = ArgumentCaptor.forClass(String.class);

        String result = spy.sendSMSChallengeCodeToPhoneNumber(phoneNumber);
        verify(cacheService, times(1)).put(cacheKeyCaptor.capture(), phoneNumberCaptor.capture(), codeCaptor.capture());
        assertEquals(cacheKeyCaptor.getValue(), cacheKey);
        assertEquals(phoneNumberCaptor.getValue(), phoneNumber);
        assertThat(result).isInstanceOf(String.class).containsOnlyDigits().hasSize(6);
    }

    @Test
    public void sendSMSChallengeCodeToPhoneNumberWhenChallengeCodeIsntSent() {
        String phoneNumber = "1234567890";
        String errorMessage = "error sending sms challenge to " + phoneNumber;
        SMSChallengeCodeService spy = spy(smsChallengeCodeService);
        when(smss.sendSMS(anyString(), anyString())).thenReturn(false);

        String result = spy.sendSMSChallengeCodeToPhoneNumber(phoneNumber);
        verify(cacheService, never()).put(any(), any(), any());
        assertEquals(result, errorMessage);
    }

    @Test
    public void isAValidSMSChallengeCodeWhenCodeValid() {
        String phoneNumber = "1234567890";
        String sentCode = "123456";
        when(cacheService.get(any(), any())).thenReturn(sentCode);
        ArgumentCaptor<String> phoneNumberCaptor = ArgumentCaptor.forClass(String.class);
        boolean result = smsChallengeCodeService.isAValidSMSChallengeCode(phoneNumber, sentCode);
        verify(cacheService, times(1)).get(any(), phoneNumberCaptor.capture());
        assertEquals(phoneNumberCaptor.getValue(), phoneNumber);
        assertTrue(result);
    }

    @Test
    public void isAValidSMSChallengeCodeWhenCodeNotInCache() {
        String phoneNumber = "1234567890";
        String sentCode = "123456";
        when(cacheService.get(any(), any())).thenReturn(null);
        ArgumentCaptor<String> phoneNumberCaptor = ArgumentCaptor.forClass(String.class);
        boolean result = smsChallengeCodeService.isAValidSMSChallengeCode(phoneNumber, sentCode);
        verify(cacheService, times(1)).get(any(), phoneNumberCaptor.capture());
        assertEquals(phoneNumberCaptor.getValue(), phoneNumber);
        assertFalse(result);
    }

    @Test
    public void isAValidSMSChallengeCodeWhenCodeDoesntMatchCache() {
        String phoneNumber = "1234567890";
        String cachedCode = "456789";
        String sentCode = "123456";
        when(cacheService.get(any(), any())).thenReturn(cachedCode);
        ArgumentCaptor<String> phoneNumberCaptor = ArgumentCaptor.forClass(String.class);
        boolean result = smsChallengeCodeService.isAValidSMSChallengeCode(phoneNumber, sentCode);
        verify(cacheService, times(1)).get(any(), phoneNumberCaptor.capture());
        assertEquals(phoneNumberCaptor.getValue(), phoneNumber);
        assertFalse(result);
    }
}
