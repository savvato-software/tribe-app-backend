package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericResponseDTO;

public interface GenericResponseService {
    public GenericResponseDTO createDTO(String message);
    public GenericResponseDTO createDTO(boolean val);
    public GenericResponseDTO createDTO(Iterable<String> message);

}
