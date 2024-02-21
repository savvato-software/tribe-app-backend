package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericMessageDTO;

public interface GenericMessageService {
    public GenericMessageDTO createDTO(String message);
    public GenericMessageDTO createDTO(boolean val);
    public GenericMessageDTO createDTO(Iterable<String> message);

}
