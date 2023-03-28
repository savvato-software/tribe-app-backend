package com.savvato.tribeapp.services;

import com.savvato.tribeapp.repositories.*;
import org.springframework.stereotype.Service;

@Service
public class AttributesServiceImpl implements AttributesService{

    RejectedNonEnglishWordRepository rejectedNonEnglishWordRepository;
    private final VerbRepository verbRepository;
    private final NounRepository nounRepository;
    private final AdverbRepository adverbRepository;
    private final PrepositionRepository prepositionRepository;

    public AttributesServiceImpl(VerbRepository verbRepository,
                                 NounRepository nounRepository,
                                 AdverbRepository adverbRepository,
                                 PrepositionRepository prepositionRepository
    ) {
        this.verbRepository = verbRepository;
        this.nounRepository = nounRepository;
        this.adverbRepository = adverbRepository;
        this.prepositionRepository = prepositionRepository;
    }

    public boolean isPhraseValid(String verb, String noun, String adverb, String preposition) {
        boolean rtn = true;
        rtn = rtn && isWordPreviouslyRejected(verb);
        rtn = rtn && isWordPreviouslyRejected(noun);
        rtn = rtn && isWordPreviouslyRejected(adverb);
        rtn = rtn && isWordPreviouslyRejected(preposition);

        return rtn;
    }

    public boolean isWordPreviouslyRejected(String word) {
        return this.rejectedNonEnglishWordRepository.findByWord(word).isPresent();
    }

    public void applyPhraseToUser(String verb, String noun, String adverb, String preposition) {
        boolean rtn = hasPhraseBeenReviewed(verb, noun, adverb, preposition);

        if (rtn) {
            // we have seen this before
            // associate it with the user
        } else {
            // we have not seen this before
            // add it to the database
            // associate it with the user
        }
    }

    public boolean hasPhraseBeenReviewed(String verb, String noun, String adverb, String preposition) {
        boolean rtn = true;
        rtn = rtn && isGivenVerbFound(verb);
        rtn = rtn && isGivenNounFound(noun);
        rtn = rtn && isGivenAdverbFound(adverb);
        rtn = rtn && isGivenPrepositionFound(preposition);

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
}
