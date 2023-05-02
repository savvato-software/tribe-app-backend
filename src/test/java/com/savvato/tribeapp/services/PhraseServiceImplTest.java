package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.*;
import com.savvato.tribeapp.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith({SpringExtension.class})
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

    // Test that UserPhraseRepository is called once when calling ApplyPhraseToUser and phrase has been approved
    @Test
    public void testApplyPhraseToUserForOneCallToUserPhraseRepository() {

        User user1 = getUser1();

        Adverb testAdverb = getTestAdverb1();
        Verb testVerb = getTestVerb1();
        Preposition testPreposition = getTestPreposition1();
        Noun testNoun = getTestNoun1();

        Phrase testPhrase = getTestPhrase1();

        UserPhrase userPhrase = new UserPhrase();
        userPhrase.setUserId(user1.getId());
        userPhrase.setPhraseId(testPhrase.getId());

        Mockito.when(adverbRepository.findByWord(anyString())).thenReturn(Optional.of(testAdverb));
        Mockito.when(verbRepository.findByWord(anyString())).thenReturn(Optional.of(testVerb));
        Mockito.when(prepositionRepository.findByWord(anyString())).thenReturn(Optional.of(testPreposition));
        Mockito.when(nounRepository.findByWord(anyString())).thenReturn(Optional.of(testNoun));

        Mockito.when(phraseRepository.findByAdverbIdAndVerbIdAndPrepositionIdAndNounId(any(Long.class), any(Long.class), any(Long.class), any(Long.class))).thenReturn(Optional.of(testPhrase));

        Mockito.when(userPhraseRepository.save(Mockito.any())).thenReturn(userPhrase);

        phraseService.applyPhraseToUser(user1.getId(), "testAdverb", "testVerb", "testPreposition", "testNoun");

        verify(userPhraseRepository, times(1)).save(Mockito.any());

    }

    // Test that UserPhraseRepository is called once when calling ApplyPhraseToUser and phrase has not been approved
}
