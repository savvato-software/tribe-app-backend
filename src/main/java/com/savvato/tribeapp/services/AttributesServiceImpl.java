package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.dto.PhraseDTO;
import com.savvato.tribeapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttributesServiceImpl implements AttributesService{

    @Autowired
    PhraseService phraseService;

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

    @Override
    public Optional<List<AttributeDTO>> getAttributesByUserId(Long userId) {

        List<AttributeDTO> attributes = new ArrayList<>();

        // Get all user phrases as phraseDTOs
        Optional<List<PhraseDTO>> optUserPhraseDTOs = phraseService.getListOfPhraseDTOByUserId(userId);

        // If there are phrases, build DTO and add to attributes list
        if(optUserPhraseDTOs.isPresent()) {
            List<PhraseDTO> phrases = optUserPhraseDTOs.get();
            for(PhraseDTO phrase : phrases) {
                AttributeDTO attributeDTO = AttributeDTO.builder()
                        .phrase(phrase)
                        .build();
                attributes.add(attributeDTO);
            }
        }

        // Return Optional of attributes or empty Optional if there aren't any
        return (attributes.isEmpty() ? Optional.empty() : Optional.of(attributes));
    }
}
