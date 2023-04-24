package com.savvato.tribeapp.services;

import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.dto.PhraseDTO;
import com.savvato.tribeapp.entities.*;
import com.savvato.tribeapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
//////////import java.util.logging.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class PhraseServiceImpl implements PhraseService {

    /////static final Logger LOGGER = Logger.getLogger(PhraseServiceImpl.class.getName());
    Logger logger = LoggerFactory.getLogger(PhraseServiceImpl.class);


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
    RejectedPhraseRepository rejectedPhraseRepository;

    @Autowired
    RejectedNonEnglishWordRepository rejectedNonEnglishWordRepository;

    @Autowired
    UserPhraseService userPhraseService;

    @Autowired
    UserPhraseRepository userPhraseRepository;

    @Override
    public boolean isPhraseValid(String adverb, String verb, String preposition, String noun) {

        String adverbLowerCase = changeToLowerCase(adverb);
        String verbLowerCase = changeToLowerCase(verb);
        String prepositionLowerCase = changeToLowerCase(preposition);
        String nounLowerCase = changeToLowerCase(noun);

        if(isMissingVerbOrNoun(verbLowerCase,nounLowerCase) ||
                isAnyWordRejected(adverbLowerCase, verbLowerCase, prepositionLowerCase, nounLowerCase) ||
                isPhrasePreviouslyRejected(adverbLowerCase, verbLowerCase, prepositionLowerCase, nounLowerCase)) {
            ////////////////LOGGER.warning("Phrase is not valid.");
            logger.warn("Phrase is not valid.");

            return false;
        }

        return true;
    }

    public String changeToLowerCase(String word) {
        if(word != null && !word.trim().isEmpty()){
            return word.toLowerCase();
        }
        return word;
    }

    public boolean isMissingVerbOrNoun(String verb, String noun) {
        if (verb == null || verb.trim().isEmpty()) {
            throw new IllegalArgumentException("Phrase is missing a verb.");
        }
        if (noun == null || noun.trim().isEmpty()) {
            throw new IllegalArgumentException("Phrase is missing a noun.");
        }
        return false;
    }

    public boolean isAnyWordRejected(String adverb, String verb, String preposition, String noun) {

        List<String> words = Arrays.asList(adverb, verb, preposition, noun);
        for(String word: words) {
            if(isWordPreviouslyRejected(word)){
                ///////////LOGGER.warning(word + " exists in rejected words.");
                logger.warn(word + " exists in rejected words.");
                return true;
            }
        }

        return false;
    }

    public boolean isWordPreviouslyRejected(String word) {
        return this.rejectedNonEnglishWordRepository.findByWord(word).isPresent();
    }

    public boolean isPhrasePreviouslyRejected(String adverb, String verb, String preposition, String noun) {
        StringBuilder rejectedPhraseSB = new StringBuilder();

        if(adverb != null && !adverb.trim().isEmpty()) { rejectedPhraseSB.append(adverb + " "); }
        rejectedPhraseSB.append(verb + " ");
        if(preposition != null && !preposition.trim().isEmpty()) { rejectedPhraseSB.append(preposition + " "); }
        rejectedPhraseSB.append(noun);

        String rejectedPhraseString = rejectedPhraseSB.toString().trim();

        Optional<RejectedPhrase> rejectedPhrase = rejectedPhraseRepository.findByRejectedPhrase(rejectedPhraseString);

        if(rejectedPhrase.isPresent()) {
            ////////////////LOGGER.warning(rejectedPhraseString + " exits in rejected phrases.");
            logger.warn(rejectedPhraseString + " exits in rejected phrases.");
            return true;
        }

        return false;
    }

    @Override
    public void applyPhraseToUser(Long userId, String adverb, String verb, String preposition, String noun) {

        String adverbLowerCase = changeToLowerCase(adverb);
        String verbLowerCase = changeToLowerCase(verb);
        String prepositionLowerCase = changeToLowerCase(preposition);
        String nounLowerCase = changeToLowerCase(noun);

        Optional<Long> previouslyReviewedPhraseId = findPreviouslyApprovedPhraseId(adverbLowerCase, verbLowerCase, prepositionLowerCase, nounLowerCase);

        if (previouslyReviewedPhraseId.isPresent()) {
            UserPhrase userPhrase = new UserPhrase();
            userPhrase.setUserId(userId);
            userPhrase.setPhraseId(previouslyReviewedPhraseId.get());
            userPhraseRepository.save(userPhrase);
            ///////////////LOGGER.info("Phrase added to user.");
            logger.info("Phrase added to user.");

        } else {
            // we have not seen this before
            // add it to the database
            // associate it with the user
        }
    }

    @Override
    public Optional<Long> findPreviouslyApprovedPhraseId(String adverb, String verb, String preposition, String noun) {

        Long adverbId;
        Long verbId;
        Long prepositionId;
        Long nounId;

        if(findVerbIfExists(verb).isPresent()) {
            verbId = verbRepository.findByWord(verb).get().getId();
        } else {
            return Optional.empty();
        }

        if(findNounIfExists(noun).isPresent()) {
            nounId = nounRepository.findByWord(noun).get().getId();
        } else {
            return Optional.empty();
        }

        if (adverb == null || adverb.trim().isEmpty()) {
            adverbId = Constants.NULL_VALUE_ID;
        } else if (findAdverbIfExists(adverb).isPresent()) {
            adverbId = adverbRepository.findByWord(adverb).get().getId();
        } else {
            return Optional.empty();
        }

        if (preposition == null || preposition.trim().isEmpty()) {
            prepositionId = Constants.NULL_VALUE_ID;
        } else if (findPrepositionIfExists(preposition).isPresent()) {
            prepositionId = prepositionRepository.findByWord(preposition).get().getId();
        } else {
            return Optional.empty();
        }

        Optional<Phrase> reviewedPhrase = phraseRepository.findByAdverbIdAndVerbIdAndPrepositionIdAndNounId(adverbId, verbId, prepositionId, nounId);

        if (reviewedPhrase.isPresent()) {
            return Optional.of(reviewedPhrase.get().getId());
        }
        return Optional.empty();
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
