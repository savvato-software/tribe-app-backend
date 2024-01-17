package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericMessageDTO;

@Service
public class GenericMessageServiceImpl implements GenericMessageService{
    public GenericMessageDTO createMessageDTO( String message ) {

        return GenericMessageDTO.builder()
                .responseMessage(message)
                .build();
    }

    public GenericMessageDTO createBooleanDTO (Boolean val) {

        return GenericMessageDTO.builder()
                .booleanMessage(val)
                .build();
    }

    public GenericMessageDTO createIterableDTO (Iterable<String> message) {

        return GenericMessageDTO.builder()
                .iterableMessage(message)
                .build();
    }
}

//AUTHAPIController User
//ConnectAPIController get