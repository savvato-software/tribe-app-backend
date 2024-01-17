package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericMessageDTO;

public class GenericMessageServiceImpl {
    public GenericMessageDTO createMessageDTO( String message ) {

        return GenericMessageDTO.builder()
                .responseMessage(message)
                .build();
    }
}
