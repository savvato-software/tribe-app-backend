package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.repositories.PhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhraseServiceImpl implements PhraseService {
    @Autowired
    PhraseRepository phraseRepository;

    @Override
    public Long getLastAssignedPhraseId() {
        return lastAssignedPhraseId;
    }
    public void setLastAssignedPhraseId(Long id) {
        this.lastAssignedPhraseId = id;
    }
    Long lastAssignedPhraseId = 0L;
    public Optional<Phrase> getReviewPhrase() {
        Optional<Phrase> opt = phraseRepository.findNextAvailablePhrase(lastAssignedPhraseId);
        if (opt.isPresent()) {
            setLastAssignedPhraseId(opt.get().getId());
            return opt;
        }
        return Optional.empty();
    }



}
