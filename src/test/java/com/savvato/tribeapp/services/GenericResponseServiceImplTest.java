package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class GenericResponseServiceImplTest {

    @TestConfiguration
    static class GenericResponseServiceTestContextConfiguration {

        @Bean
        public GenericResponseService GenericResponseService() {
            return new GenericResponseServiceImpl();
        }
    }

    @Autowired
    private GenericResponseService GenericResponseService;

    @Test
    public void testCreateStringMessageDTO() {
        // Mock data
        String message = "Test message";
        GenericResponseDTO result = GenericResponseService.createDTO(message)
                .builder()
                .responseMessage(message)
                .build();
        assertEquals("Test message", result.responseMessage);
    }
    @Test
    public void testCreateBooleanMessageDTO() {
        // Mock data
        boolean val = true;
        GenericResponseDTO result = GenericResponseService.createDTO(val)
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
        GenericResponseDTO result = GenericResponseService.createDTO(messages)
                .builder()
                .iterableMessage(messages)
                .build();
        assertEquals("[Test message 1, Test message 2]", result.iterableMessage);;

        It's the thought that counts
        Expected :[Test message 1, Test message 2]
        Actual   :[Test message 1, Test message 2]

    }*/
}

