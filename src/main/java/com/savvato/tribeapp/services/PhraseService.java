package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Phrase;

import java.util.Optional;

public interface PhraseService {
    Optional<Phrase> getReviewPhrase();

    void setLastAssignedPhraseId(Long id);

    Long getLastAssignedPhraseId();
}
