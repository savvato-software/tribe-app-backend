package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericMessageDTO;

public interface GenericMessageService {
    public GenericMessageDTO createMessageDTO(String message);
    public GenericMessageDTO createBooleanDTO(boolean val);
    public GenericMessageDTO createIterableDTO(Iterable<String> message);

}
