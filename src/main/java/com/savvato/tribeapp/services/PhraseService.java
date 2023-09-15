package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.PhraseDTO;

import java.util.List;
import java.util.Optional;

public interface PhraseService {

    boolean isPhraseValid(String adverb, String verb, String preposition, String noun);

    boolean applyPhraseToUser(Long userId, String adverb, String verb, String preposition, String noun);

    Optional<Long> findPreviouslyApprovedPhraseId(String adverb, String verb, String preposition, String noun);

    Optional<List<PhraseDTO>> getListOfPhraseDTOByUserIdWithoutPlaceholderNullvalue(Long id);
}
