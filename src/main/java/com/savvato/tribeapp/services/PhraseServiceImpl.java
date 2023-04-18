package com.savvato.tribeapp.services;

import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.dto.PhraseDTO;
import com.savvato.tribeapp.entities.*;
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
    UserPhraseRepository userPhraseRepository;

    @Autowired
    PhraseRepository phraseRepository;

    @Autowired
    AdverbRepository adverbRepository;

    @Autowired
    VerbRepository verbRepository;

    @Autowired
    PrepositionRepository prepositionRepository;

    @Autowired
    NounRepository nounRepository;

    @Autowired
    RejectedNonEnglishWordRepository rejectedNonEnglishWordRepository;

    @Override
    public boolean isPhraseValid(String adverb, String verb, String preposition, String noun) {
        boolean rtn = true;
        rtn = rtn && !isWordPreviouslyRejected(adverb);
        rtn = rtn && !isWordPreviouslyRejected(verb);
        rtn = rtn && !isWordPreviouslyRejected(preposition);
        rtn = rtn && !isWordPreviouslyRejected(noun);

        return rtn;
    }

    public boolean isWordPreviouslyRejected(String word) {
        return this.rejectedNonEnglishWordRepository.findByWord(word).isPresent();
    }

    @Override
    public void applyPhraseToUser(Long userId, String adverb, String verb, String preposition, String noun) {
        Optional<Long> reviewedPhraseId = checkIfPhraseHasBeenReviewed(adverb, verb, preposition, noun);

        if (reviewedPhraseId.isPresent()) {
            UserPhrase userPhrase = new UserPhrase();
            userPhrase.setUserId(userId);
            userPhrase.setPhraseId(reviewedPhraseId.get());
            userPhraseRepository.save(userPhrase);
            System.out.println("phrase exists: " + reviewedPhraseId.get());
            // we have seen this before
            // associate it with the user
        } else {
            System.out.println(("phrase does NOT exist!"));
            // we have not seen this before
            // add it to the database
            // associate it with the user
        }
    }

    public Optional<Long> checkIfPhraseHasBeenReviewed(String adverb, String verb, String preposition, String noun) {

        // Dev note: do not check for null verb or noun values passed in. API controller does this.
        Long phraseId = null;
        Long adverbId = null;
        Long verbId = null;
        Long prepositionId = null;
        Long nounId = null;

        // Check if the verb and noun already exist in their respective repos and retrieve ids
        if (findVerbIfExists(verb).isPresent() && findNounIfExists(noun).isPresent()) {
            verbId = verbRepository.findByWord(verb).get().getId();
            nounId = nounRepository.findByWord(noun).get().getId();

            // Check if adverb and preposition exist and retrieve their ids or add nullvalue id from Constants
            if (adverb != null && findAdverbIfExists(adverb).isPresent()) {
                adverbId = adverbRepository.findByWord(adverb).get().getId();
            } else {
                adverbId = Constants.NULL_VALUE_ID;
            }
            if (preposition != null && findPrepositionIfExists(preposition).isPresent()) {
                prepositionId = prepositionRepository.findByWord(preposition).get().getId();
            } else {
                prepositionId = Constants.NULL_VALUE_ID;
            }

            // check phrase repo to see if this combination of ids exists as a phrase
            Optional<Phrase> reviewedPhrase = phraseRepository.findByAdverbIdAndVerbIdAndPrepositionIdAndNounId(adverbId, verbId, prepositionId, nounId);
            if(reviewedPhrase.isPresent()) {
                phraseId = reviewedPhrase.get().getId();
            }

        }

        return Optional.ofNullable(phraseId);
    }

    public Optional<Adverb> findAdverbIfExists(String adverb) {
        return this.adverbRepository.findByWord(adverb);
    }

    public Optional<Verb> findVerbIfExists(String verb) {
        return this.verbRepository.findByWord(verb);
    }

    public Optional<Preposition> findPrepositionIfExists(String preposition) {
        return this.prepositionRepository.findByWord(preposition);

    }

    public Optional<Noun> findNounIfExists(String noun) {
        return this.nounRepository.findByWord(noun);
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
