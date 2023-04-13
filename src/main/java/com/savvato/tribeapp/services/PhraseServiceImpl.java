package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.PhraseDTO;
import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PhraseServiceImpl implements PhraseService {

    @Autowired
    UserPhraseService userPhraseService;

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    AdverbRepository adverbRepository;

    @Autowired
    NounRepository nounRepository;

    @Autowired
    PrepositionRepository prepositionRepository;

    @Autowired
    VerbRepository verbRepository;

    @Autowired
    RejectedNonEnglishWordRepository rejectedNonEnglishWordRepository;

    public boolean isPhraseValid(String adverb, String verb, String preposition, String noun) {
        boolean rtn = true;
        rtn = rtn && isWordPreviouslyRejected(adverb);
        rtn = rtn && isWordPreviouslyRejected(verb);
        rtn = rtn && isWordPreviouslyRejected(preposition);
        rtn = rtn && isWordPreviouslyRejected(noun);

        return rtn;
    }

    public boolean isWordPreviouslyRejected(String word) {
        return this.rejectedNonEnglishWordRepository.findByWord(word).isPresent();
    }

    public void applyPhraseToUser(String adverb, String verb, String preposition, String noun) {
        boolean rtn = hasPhraseBeenReviewed(adverb, verb, preposition, noun);

        if (rtn) {
            // we have seen this before
            // associate it with the user
        } else {
            // we have not seen this before
            // add it to the database
            // associate it with the user
        }
    }

    public boolean hasPhraseBeenReviewed(String adverb, String verb, String preposition, String noun) {
        boolean rtn = true;
        rtn = rtn && isGivenVerbFound(adverb);
        rtn = rtn && isGivenNounFound(verb);
        rtn = rtn && isGivenAdverbFound(preposition);
        rtn = rtn && isGivenPrepositionFound(noun);

        return rtn;
    }

    public boolean isGivenVerbFound(String verb) {
        return this.verbRepository.findByWord(verb).isPresent();
    }

    public boolean isGivenNounFound(String noun) {
        return this.nounRepository.findByWord(noun).isPresent();
    }

    public boolean isGivenAdverbFound(String adverb) {
        return this.adverbRepository.findByWord(adverb).isPresent();
    }

    public boolean isGivenPrepositionFound(String preposition) {
        return this.prepositionRepository.findByWord(preposition).isPresent();
    }

    @Override
    public Optional<List<PhraseDTO>> getListOfPhraseDTOByUserId(Long userId) {

        // create list of PhraseDtos
        List<PhraseDTO> phraseDTOS = new ArrayList<>();

        // Create list of phrase ids
        Optional<List<Long>> optPhraseIds = userPhraseService.findPhraseIdsByUserId(userId);

        // Create list of all phrases from those phrase ids
        if (optPhraseIds.isPresent()) {
            List<Long> phraseIds = optPhraseIds.get();
            List<Phrase> phrases = new ArrayList<>();

            // loop through list of phrase ids and add each phrase to list
            for (Long phraseId : phraseIds) {
                Optional<Phrase> optPhrase = phraseRepository.findPhraseByPhraseId(phraseId);
                if (optPhrase.isPresent()) {
                    phrases.add(optPhrase.get());
                } else {
                    throw new IllegalStateException("phrase not found");
                }
            }

            // loop through phrases and get words
            for (Phrase phrase : phrases) {
                PhraseDTO phraseDTO = PhraseDTO.builder().build();

                Optional<String> optAdverb = adverbRepository.findAdverbById(phrase.getAdverbId());
                Optional<String> optVerb = verbRepository.findVerbById(phrase.getVerbId());
                Optional<String> optPreposition = prepositionRepository.findPrepositionById(phrase.getPrepositionId());
                Optional<String> optNoun = nounRepository.findNounById(phrase.getNounId());

                if (optAdverb.isPresent()) {
                    phraseDTO.adverb = optAdverb.get();
                }
                if (optVerb.isPresent()) {
                    phraseDTO.verb = optVerb.get();
                }
                if (optPreposition.isPresent()) {
                    phraseDTO.preposition = optPreposition.get();
                }
                if (optNoun.isPresent()) {
                    phraseDTO.noun = optNoun.get();
                }

                phraseDTOS.add(phraseDTO);
            }

            return Optional.of(phraseDTOS);

        } else {

            return Optional.empty();

        }

    }
}
