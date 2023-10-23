package com.savvato.tribeapp.services;

import java.util.List;
import java.util.Optional;

public interface UserPhraseService {
    Optional<List<Long>> findPhraseIdsByUserId(Long userId);

    void deletePhraseFromUser(Long phraseId, Long userId);
}
