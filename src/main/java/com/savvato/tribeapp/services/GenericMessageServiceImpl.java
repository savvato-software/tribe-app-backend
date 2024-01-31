package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericMessageDTO;
import org.springframework.stereotype.Service;

@Service
public class GenericMessageServiceImpl implements GenericMessageService{
    public GenericMessageDTO createDTO( String message ) {

        return GenericMessageDTO.builder()
                .responseMessage(message)
                .build();
    }

    public GenericMessageDTO createDTO (boolean val) {

        return GenericMessageDTO.builder()
                .booleanMessage(val)
                .build();
    }

    public GenericMessageDTO createDTO (Iterable<String> message) {

        return GenericMessageDTO.builder()
                .iterableMessage(message)
                .build();
    }
}

//AUTHAPIController User
//ConnectAPIController get