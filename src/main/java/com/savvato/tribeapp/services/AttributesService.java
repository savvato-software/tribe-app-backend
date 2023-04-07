package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.AttributeDTO;

import java.util.List;
import java.util.Optional;

public interface AttributesService {

    boolean isPhraseValid(String verb, String noun, String adverb, String preposition);
    void applyPhraseToUser(String verb, String noun, String adverb, String preposition);

    Optional<List<AttributeDTO>> getAttributesByUserId(Long id);

}

