package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class GenericResponseServiceImpl implements GenericResponseService{
    public GenericResponseDTO createDTO( String message ) {

        return GenericResponseDTO.builder()
                .responseMessage(message)
                .build();
    }

    public GenericResponseDTO createDTO (boolean val) {

        return GenericResponseDTO.builder()
                .booleanMessage(val)
                .build();
    }

    public GenericResponseDTO createDTO (Iterable<String> message) {

        return GenericResponseDTO.builder()
                .iterableMessage(message)
                .build();
    }
}

//AUTHAPIController User
//ConnectAPIController get