package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.dto.PhraseDTO;
import com.savvato.tribeapp.repositories.UserPhraseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AttributesServiceImplTest {

    @TestConfiguration
    static class AttributesServiceTestContextConfiguration {
        @Bean
        public AttributesService attributesService() {
            return new AttributesServiceImpl();
        }
    }

    @Autowired
    AttributesService attributesService;
    @MockBean
    PhraseService phraseService;
    @MockBean
    UserPhraseRepository userPhraseRepository;

    @Test
    public void getAttributesByUserIdWhenAttributesExist() {
        Long userId = 1L;
        Map<PhraseDTO, Integer> phraseInformation =
                Map.of(
                        PhraseDTO.builder().adverb("competitively").verb("plays").noun("chess").build(), 1,
                        PhraseDTO.builder()
                                .adverb("enthusiastically")
                                .verb("volunteers")
                                .preposition("for")
                                .noun("UNICEF")
                                .build(), 2);
        List<AttributeDTO> attributes = phraseInformation
                .entrySet()
                .stream()
                .map(
                        entry -> AttributeDTO.builder().phrase(entry.getKey()).userCount(entry.getValue()).build()
                )
                .toList();
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        Optional<List<AttributeDTO>> expected = Optional.of(attributes);
        when(phraseService.getPhraseInformationByUserId(anyLong()))
                .thenReturn(Optional.of(phraseInformation));
        Optional<List<AttributeDTO>> actual = attributesService.getAttributesByUserId(userId);
        verify(phraseService, times(1)).getPhraseInformationByUserId(userIdArgumentCaptor.capture());
        assertEquals(userIdArgumentCaptor.getValue(), userId);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getAttributesByUserIdWhenAttributesDontExist() {
        Long userId = 1L;
        Optional<List<AttributeDTO>> expected = Optional.of(new ArrayList<>());
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(phraseService.getPhraseInformationByUserId(anyLong()))
                .thenReturn(Optional.empty());

        Optional<List<AttributeDTO>> actual = attributesService.getAttributesByUserId(userId);

        verify(phraseService, times(1)).getPhraseInformationByUserId(userIdArgumentCaptor.capture());
        assertEquals(userIdArgumentCaptor.getValue(), userId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getNumberOfUsersWithAttributeWhenAttributeExists() {
        Long attributeId = 1L;
        Integer userCount = 2;
        Optional<Integer> expected = Optional.of(userCount);
        ArgumentCaptor<Long> attributeIdCaptor = ArgumentCaptor.forClass(Long.class);
        when(userPhraseRepository.countUsersWithAttribute(anyLong())).thenReturn(userCount);
        Optional<Integer> actual = attributesService.getNumberOfUsersWithAttribute(attributeId);
        verify(userPhraseRepository, times(1)).countUsersWithAttribute(attributeIdCaptor.capture());
        assertEquals(attributeIdCaptor.getValue(), attributeId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void getNumberOfUsersWithAttributeWhenErrorOccurs() {
        Long attributeId = 1L;
        String errorMessage = "An error occurred";
        Optional<Integer> expected = Optional.empty();
        ArgumentCaptor<Long> attributeIdCaptor = ArgumentCaptor.forClass(Long.class);
        when(userPhraseRepository.countUsersWithAttribute(anyLong())).thenThrow(new IllegalArgumentException(errorMessage));
        Optional<Integer> actual = attributesService.getNumberOfUsersWithAttribute(attributeId);

        verify(userPhraseRepository, times(1)).countUsersWithAttribute(attributeIdCaptor.capture());
        assertEquals(attributeIdCaptor.getValue(), attributeId);
        assertThat(actual).isEqualTo(expected);

    }
}
