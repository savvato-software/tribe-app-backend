package com.savvato.tribeapp.services;

import com.savvato.tribeapp.constants.Constants;
import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ToBeReviewedServiceImplTest extends AbstractServiceImplTest {

    @TestConfiguration
    static class PhraseServiceTestContextConfiguration {
        @Bean
        public ToBeReviewedService phraseService() {
            return new ToBeReviewedServiceImpl();
        }
    }
    @Autowired
    ToBeReviewedService toBeReviewedService;

    @MockBean
    ToBeReviewedRepository toBeReviewedRepository;

    // test that the method is returning the database's results correctly
    @Test
    public void testGetReviewPhraseHappyPath() {
        ToBeReviewed expectedToBeReviewed = new ToBeReviewed();
        expectedToBeReviewed.setId(1L);
        expectedToBeReviewed.setHasBeenGroomed(true);
        expectedToBeReviewed.setAdverb("competitively");
        expectedToBeReviewed.setVerb("programs");
        expectedToBeReviewed.setPreposition("with");
        expectedToBeReviewed.setNoun("Python");

        Mockito.when(toBeReviewedRepository.findNextReviewEligible(any(Long.class))).thenReturn(Optional.of(expectedToBeReviewed));

        Optional<ToBeReviewed> rtn = toBeReviewedService.getReviewPhrase();

        assertEquals(rtn.get(), expectedToBeReviewed);
        assertEquals(toBeReviewedService.getLastAssignedForReview(), expectedToBeReviewed.getId());
    }

    // test that when getReviewPhrase() finds no phrases to review, it returns an empty optional object
    @Test
    public void testGetReviewPhraseWhenNoNextItemIsAvailable() {

        //Ensure the repository is returning an empty optional
        Mockito.when(toBeReviewedRepository.findNextReviewEligible(any(Long.class))).thenReturn(Optional.empty());

        //Success: test that the getReviewPhrase() method returns an empty Optional object
        assertFalse(toBeReviewedService.getReviewPhrase().isPresent());
        
    }

    // TRIB-62
    @Test
    public void testSecondCallReturnsNewPhrase() {

        ToBeReviewed expectedToBeReviewed = new ToBeReviewed();
        expectedToBeReviewed.setId(1L);
        expectedToBeReviewed.setHasBeenGroomed(true);
        expectedToBeReviewed.setAdverb("competitively");
        expectedToBeReviewed.setVerb("programs");
        expectedToBeReviewed.setPreposition("with");
        expectedToBeReviewed.setNoun("Python");

        Mockito.when(toBeReviewedRepository.findNextReviewEligible(any(Long.class))).thenReturn(Optional.of(expectedToBeReviewed));

        Optional<ToBeReviewed> rtn = toBeReviewedService.getReviewPhrase();

        assertEquals(rtn.get(), expectedToBeReviewed);
        assertEquals(toBeReviewedService.getLastAssignedForReview(), expectedToBeReviewed.getId());

        expectedToBeReviewed.setId(2L);
        expectedToBeReviewed.setHasBeenGroomed(true);
        expectedToBeReviewed.setAdverb("uncompetitively");
        expectedToBeReviewed.setVerb("codes");
        expectedToBeReviewed.setPreposition("without");
        expectedToBeReviewed.setNoun("Scala");

        Optional<ToBeReviewed> rtnTwo = toBeReviewedService.getReviewPhrase();

        assertEquals(rtnTwo.get(), expectedToBeReviewed);
        assertEquals(toBeReviewedService.getLastAssignedForReview(), expectedToBeReviewed.getId());
    }

    @Test
    public void testGetReviewPhraseWithoutPlaceholderNullvalueReturnsEmptyString() {
        String testWord = "test";
        String testEmptyString = "";

        ToBeReviewed testTbr = new ToBeReviewed();
        testTbr.setId(1L);
        testTbr.setHasBeenGroomed(true);
        testTbr.setAdverb(Constants.NULL_VALUE_WORD);
        testTbr.setVerb(testWord);
        testTbr.setPreposition(Constants.NULL_VALUE_WORD);
        testTbr.setNoun(testWord);

        Mockito.when(toBeReviewedRepository.findNextReviewEligible(anyLong())).thenReturn(Optional.of(testTbr));

        Optional<ToBeReviewed> tbrOptional = toBeReviewedService.getReviewPhrase();
        ToBeReviewed tbr = tbrOptional.get();

        assertEquals(tbr.getAdverb(),testEmptyString);
        assertEquals(tbr.getPreposition(),testEmptyString);
    }
}
