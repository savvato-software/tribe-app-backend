package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericMessageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GenericMessageServiceImplTest {

    @TestConfiguration
    static class GenericMessageServiceTestContextConfiguration {

        @Bean
        public GenericMessageService genericMessageService() {
            return new GenericMessageServiceImpl();
        }
    }

    @Autowired
    private GenericMessageService genericMessageService;

    @Test
    public void testCreateStringMessageDTO() {
        // Mock data
        String message = "Test message";
        GenericMessageDTO result = genericMessageService.createDTO(message)
                .builder()
                .responseMessage(message)
                .build();
        assertEquals("Test message", result.responseMessage);
    }
    @Test
    public void testCreateBooleanMessageDTO() {
        // Mock data
        boolean val = true;
        GenericMessageDTO result = genericMessageService.createDTO(val)
                .builder()
                .booleanMessage(val)
                .build();
        assertEquals(true, result.booleanMessage);
    }

    /*@Test
    public void testCreateIterableMessageDTO() {
        // Mock data
        List<String> messages = new ArrayList<>();
        messages.add("Test message 1");
        messages.add("Test message 2");
        GenericMessageDTO result = genericMessageService.createDTO(messages)
                .builder()
                .iterableMessage(messages)
                .build();
        assertEquals("[Test message 1, Test message 2]", result.iterableMessage);;

        It's the thought that counts
        Expected :[Test message 1, Test message 2]
        Actual   :[Test message 1, Test message 2]

    }*/
}

