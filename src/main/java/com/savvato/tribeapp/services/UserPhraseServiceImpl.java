package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.repositories.UserPhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserPhraseServiceImpl implements UserPhraseService{

    @Autowired
    UserPhraseRepository userPhraseRepository;

    @Autowired
    PhraseServiceImpl phraseService;

    @Override
    public Optional<List<Phrase>> findPhrasesByUserId(Long userId) {

        // variable to hold list of all user phrases that will be returned
        List<Phrase> phrases = new ArrayList<>();

        // access user phrase repo to get a list of all phrase ids associated with user
        Optional<List<Long>> optPhraseIdsForUser = userPhraseRepository.findPhraseIdsByUserId(userId);

        if (optPhraseIdsForUser.isPresent()){
            // Loop through list of phrase ids and call phrase service to build phrase
            List<Long> phraseIds = optPhraseIdsForUser.get();
            for(Long phraseId : phraseIds){
                Optional<Phrase> optCompletePhrase = phraseService.getPhraseWords(phraseId);
                if(optCompletePhrase.isPresent()) {
                    Phrase phrase = optCompletePhrase.get();
                    phrases.add(phrase);
                }
            }
            // ? Do we still use Optional? could there be no results? The enclosing if statement checks for any phrase IDs. If there are IDs, then the call to the phrase service should be guaranteed to return at least one phrase.
            return Optional.of(phrases);

            //do we need else? if we just return optional of phrases and there aren't any phrases, would it automatically be empty?
        } else {
            return Optional.empty();
        }

    }
}
