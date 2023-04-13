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

    @Override
    public boolean isPhraseValid(String verb, String noun, String adverb, String preposition) {

        boolean rtn = true;
        rtn = rtn && !isWordPreviouslyRejected(verb);
        rtn = rtn && !isWordPreviouslyRejected(noun);
        rtn = rtn && !isWordPreviouslyRejected(adverb);
        rtn = rtn && !isWordPreviouslyRejected(preposition);

        return rtn;
    }

    public boolean isWordPreviouslyRejected(String word) {
        return this.rejectedNonEnglishWordRepository.findByWord(word).isPresent();
    }

    @Override
    public void applyPhraseToUser(Long userId, String verb, String noun, String adverb, String preposition) {
        Optional<Long> reviewedPhraseId = checkIfPhraseHasBeenReviewed(verb, noun, adverb, preposition);

        if (reviewedPhraseId.isPresent()) {
            System.out.println("phrase exists!");
            // we have seen this before
            // associate it with the user
        } else {
            System.out.println(("phrase does NOT exist!"));
            // we have not seen this before
            // add it to the database
            // associate it with the user
        }
    }

    public Optional<Long> checkIfPhraseHasBeenReviewed(String verb, String noun, String adverb, String preposition) {

        // Dev note: do not check for null verb or noun values passed in. API controller does this.
        Optional<Long> reviewedPhraseId = Optional.empty();
        Long verbId = null;
        Long nounId = null;
        Long adverbId = null;
        Long prepositionId = null;

        // Check if the verb and noun already exist in their respective repos and retrieve ids
        if(findVerbIfExists(verb).isPresent() && findNounIfExists(noun).isPresent()) {
            verbId = verbRepository.findByWord(verb).get().getId();
            nounId = nounRepository.findByWord(noun).get().getId();

            // Check if adverb and preposition exist and retrieve their ids or add nullvalue id from Constants
            if(adverb != null && findAdverbIfExists(adverb).isPresent()) {
                adverbId = adverbRepository.findByWord(adverb).get().getId();
            } else {
                adverbId = Constants.NULL_VALUE_ID;
            }
            if(preposition != null && findPrepositionIfExists(verb).isPresent()) {
                prepositionId = prepositionRepository.findByWord(preposition).get().getId();
            } else {
                prepositionId = Constants.NULL_VALUE_ID;
            }

            // check phrase repo to see if this combination of ids exists as a phrase
            ////// figure out null values
            reviewedPhraseId = phraseRepository.findPhraseIdByAdverbIdAndVerbIdAndPrepositionIdAndNounId(adverbId, verbId, prepositionId, nounId);

        }

        return reviewedPhraseId;
    }

    public Optional<Verb> findVerbIfExists(String verb) {
        return this.verbRepository.findByWord(verb);
    }

    public Optional<Noun> findNounIfExists(String noun) {
        return this.nounRepository.findByWord(noun);
    }

    public Optional<Adverb> findAdverbIfExists(String adverb) {
        return this.adverbRepository.findByWord(adverb);
    }

    public Optional<Preposition> findPrepositionIfExists(String preposition) {
        return this.prepositionRepository.findByWord(preposition);
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
