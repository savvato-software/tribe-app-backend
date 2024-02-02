package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.dto.PhraseDTO;
import com.savvato.tribeapp.repositories.UserPhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AttributesServiceImpl implements AttributesService {

    @Autowired
    PhraseService phraseService;

    @Autowired
    UserPhraseRepository userPhraseRepository;

    @Override
    public Optional<List<AttributeDTO>> getAttributesByUserId(Long userId) {

        List<AttributeDTO> attributes = new ArrayList<>();

        // Get all user phrases as phraseDTOs
        Optional<List<PhraseDTO>> optUserPhraseDTOs = phraseService.getListOfPhraseDTOByUserIdWithoutPlaceholderNullvalue(userId);

        // If there are phrases, build DTO and add to attributes list
        if (optUserPhraseDTOs.isPresent()) {
            List<PhraseDTO> phrases = optUserPhraseDTOs.get();
            for (PhraseDTO phrase : phrases) {
                AttributeDTO attributeDTO = AttributeDTO.builder()
                        .phrase(phrase)
                        .build();
                attributes.add(attributeDTO);
            }
        }

        // Returns list of attributeDTOs. Can be empty.
        return Optional.of(attributes);
    }

    @Override
    public Optional<Integer> getNumberOfUsersWithAttribute(Long attributeId) {
        try {
            Integer numberOfUsers = userPhraseRepository.countUsersWithAttribute(attributeId);
            return Optional.of(numberOfUsers);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
