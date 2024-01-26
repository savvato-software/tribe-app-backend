package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.dto.PhraseDTO;
import com.savvato.tribeapp.repositories.UserPhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AttributesServiceImpl implements AttributesService {

    @Autowired
    PhraseService phraseService;

    @Autowired
    UserPhraseRepository userPhraseRepository;

    @Override
    public Optional<List<AttributeDTO>> getAttributesByUserId(Long userId) {


        // Get all user phrases as phraseDTOs
        Optional<Map<PhraseDTO, Integer>> optUserPhraseDTOs = phraseService.getPhraseInformationByUserId(userId);

        // If there are phrases, build DTO and add to attributes list
        if (optUserPhraseDTOs.isPresent()) {
            Map<PhraseDTO, Integer> phraseDTOUserCountMap = optUserPhraseDTOs.get();
            List<AttributeDTO> attributes = phraseDTOUserCountMap
                    .entrySet()
                    .stream()
                    .map(
                            entry -> AttributeDTO.builder().phrase(entry.getKey()).userCount(entry.getValue()).build()
                    )
                    .toList();
            return Optional.of(attributes);
        }

        // Returns list of attributeDTOs. Can be empty.
        return Optional.of(new ArrayList<>());
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
