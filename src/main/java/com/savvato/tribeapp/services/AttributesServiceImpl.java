package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.repositories.PhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttributesServiceImpl implements AttributesService{

    public Long id;
    public String noun;
    public String preposition;
    public String verb;
    public String adverb;

    @Autowired
    RejectedPhraseRepository rejectedPhraseRepository;

    @Override
    public Long getLastAssignedPhraseId() {
        return lastAssignedPhraseId;
    }

    public Optional<Phrase> getRejectedPhrase() {
        Optional<Phrase> opt = comparePhrase.findNextAvailablePhrase(lastAssignedPhraseId);
        if (opt.isPresent()) {
            setLastAssignedPhraseId(opt.get().getId());
            return opt;
        }
        return Optional.empty();
    }

}
