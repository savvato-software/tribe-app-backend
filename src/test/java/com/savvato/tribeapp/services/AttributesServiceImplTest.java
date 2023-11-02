package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.dto.PhraseDTO;
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

    @Test
    public void getAttributesByUserIdWhenAttributesExist() {
        Long userId = 1L;
        List<PhraseDTO> phraseDTOs =
                List.of(
                        PhraseDTO.builder().adverb("competitively").verb("plays").noun("chess").build(),
                        PhraseDTO.builder()
                                .adverb("enthusiastically")
                                .verb("volunteers")
                                .preposition("for")
                                .noun("UNICEF")
                                .build());
        List<AttributeDTO> attributeDTOs =
                phraseDTOs.stream().map(phrase -> AttributeDTO.builder().phrase(phrase).build()).toList();
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        Optional<List<AttributeDTO>> expected = Optional.of(attributeDTOs);
        when(phraseService.getListOfPhraseDTOByUserIdWithoutPlaceholderNullvalue(anyLong()))
                .thenReturn(Optional.of(phraseDTOs));
        Optional<List<AttributeDTO>> actual = attributesService.getAttributesByUserId(userId);
        verify(phraseService, times(1)).getListOfPhraseDTOByUserIdWithoutPlaceholderNullvalue(userIdArgumentCaptor.capture());
        assertEquals(userIdArgumentCaptor.getValue(), userId);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void getAttributesByUserIdWhenAttributesDontExist() {
        Long userId = 1L;
        Optional<List<AttributeDTO>> expected = Optional.of(new ArrayList<>());
        ArgumentCaptor<Long> userIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        when(phraseService.getListOfPhraseDTOByUserIdWithoutPlaceholderNullvalue(anyLong()))
                .thenReturn(Optional.empty());

        Optional<List<AttributeDTO>> actual = attributesService.getAttributesByUserId(userId);

        verify(phraseService, times(1)).getListOfPhraseDTOByUserIdWithoutPlaceholderNullvalue(userIdArgumentCaptor.capture());
        assertEquals(userIdArgumentCaptor.getValue(), userId);
        assertThat(actual).isEqualTo(expected);
    }
}
