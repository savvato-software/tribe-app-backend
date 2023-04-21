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
    public boolean isEveryWordValid(String adverb, String verb, String preposition, String noun) { //rename
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
            System.out.println("phrase exists in the system: phrase id is " + reviewedPhraseId.get());
            UserPhrase userPhrase = new UserPhrase();
            userPhrase.setUserId(userId);
            userPhrase.setPhraseId(reviewedPhraseId.get());
            userPhraseRepository.save(userPhrase);
            System.out.println("phrase added to user " + userId);
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
        Long verbId = findVerbIfExists(verb).isPresent() ? verbRepository.findByWord(verb).get().getId() : null;
        System.out.println("The verb id is: " + verbId); ////////
        Long prepositionId = null;
        Long nounId = findNounIfExists(noun).isPresent() ? nounRepository.findByWord(noun).get().getId() : null;
        System.out.println("The noun id is: " + nounId); ////////

        if (adverb == null || adverb.trim().isEmpty()) {
            adverbId = Constants.NULL_VALUE_ID;
            System.out.println("The nullvalue adverb id is: " + adverbId); ////////
        } else if (findAdverbIfExists(adverb).isPresent()) {
            adverbId = adverbRepository.findByWord(adverb).get().getId();
            System.out.println("The adverb id is: " + adverbId); ////////
        }

        if (preposition == null || preposition.trim().isEmpty()) {
            prepositionId = Constants.NULL_VALUE_ID;
            System.out.println("The nullvalue preposition id is: " + prepositionId); ////////
        } else if (findPrepositionIfExists(preposition).isPresent()) {
            prepositionId = prepositionRepository.findByWord(preposition).get().getId();
            System.out.println("The preposition id is: " + prepositionId); ////////
        }

        if(adverbId == null || verbId == null || prepositionId == null || nounId == null){
            System.out.println("Phrase has not been reviewed."); ////////
        } else {
            // check phrase repo to see if this combination of ids exists as a phrase
            Optional<Phrase> reviewedPhrase = phraseRepository.findByAdverbIdAndVerbIdAndPrepositionIdAndNounId(adverbId, verbId, prepositionId, nounId);
            if (reviewedPhrase.isPresent()) {
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
