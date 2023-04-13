package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.PhraseDTO;

import java.util.List;
import java.util.Optional;

public interface PhraseService {

    boolean isPhraseValid(String adverb, String verb, String preposition, String noun);

    void applyPhraseToUser(String verb, String noun, String adverb, String preposition);

    Optional<List<PhraseDTO>> getListOfPhraseDTOByUserId(Long id);
}
