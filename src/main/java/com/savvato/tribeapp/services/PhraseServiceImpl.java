package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.repositories.PhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class PhraseServiceImpl implements PhraseService {

    @Autowired
    PhraseRepository phraseRepository;

    @Override
    public Optional<Phrase> getPhraseWords(Long id) {
        Phrase phrase = new Phrase();

        //Optional<Verb> =
        return Optional.empty();
    }
}
