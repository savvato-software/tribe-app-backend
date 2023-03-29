package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.entities.UserPhrase;

import java.util.List;
import java.util.Optional;

public interface UserPhraseService {
    Optional<List<Phrase>> findPhrasesByUserId(Long userId);
}
