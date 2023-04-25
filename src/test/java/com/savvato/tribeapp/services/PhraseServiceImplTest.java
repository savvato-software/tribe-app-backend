package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.*;
import com.savvato.tribeapp.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(SpringExtension.class)
public class PhraseServiceImplTest extends AbstractServiceImplTest {

    @TestConfiguration
    static class PhraseServiceTestContextConfiguration {

        @Bean
        public PhraseService phraseService() {
            return new PhraseServiceImpl();
        }

    }

    @Autowired
    PhraseService phraseService;

    @MockBean
    PhraseRepository phraseRepository;

    @MockBean
    AdverbRepository adverbRepository;

    @MockBean
    VerbRepository verbRepository;

    @MockBean
    PrepositionRepository prepositionRepository;

    @MockBean
    NounRepository nounRepository;

    @MockBean
    RejectedPhraseRepository rejectedPhraseRepository;

    @MockBean
    RejectedNonEnglishWordRepository rejectedNonEnglishWordRepository;

    @MockBean
    UserPhraseService userPhraseService;

    @MockBean
    UserPhraseRepository userPhraseRepository;

    @MockBean
    ToBeReviewedRepository toBeReviewedRepository;

    @MockBean
    ReviewSubmittingUserRepository reviewSubmittingUserRepository;

    // test that illegal argument is thrown when method is called with a null verb or noun
    @Test
    public void testIsMissingVerbOrNounHappyPath() {

        assertThrows(IllegalArgumentException.class, () -> {
            phraseService.isPhraseValid("testAdverb", null, "testPreposition", null);
        });
    }

    // test that false is returned when method is called with a rejected word
    @Test
    public void testIsWordRejectedHappyPath() {

        String rejectedWord = "test";
        RejectedNonEnglishWord rejectedNonEnglishWord = new RejectedNonEnglishWord();
        rejectedNonEnglishWord.setId(1L);
        rejectedNonEnglishWord.setWord(rejectedWord);

        Mockito.when(rejectedNonEnglishWordRepository.findByWord(anyString())).thenReturn(Optional.of(rejectedNonEnglishWord));

        assertFalse(phraseService.isPhraseValid(rejectedWord, rejectedWord, rejectedWord, rejectedWord));
    }

    // test that false is returned when method is called with a rejected phrase
    @Test
    public void testIsPhrasePreviouslyRejectedHappyPath() {

        String rejectedString = "test test test test";
        RejectedPhrase rejectedPhrase = new RejectedPhrase();
        rejectedPhrase.setId(1L);
        rejectedPhrase.setRejectedPhrase(rejectedString);

        Mockito.when(rejectedPhraseRepository.findByRejectedPhrase(anyString())).thenReturn(Optional.of(rejectedPhrase));

        assertFalse(phraseService.isPhraseValid("test", "test", "test", "test"));
    }

}
