package com.savvato.tribeapp.services;

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
    public void getReviewPhrase() {
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
    public void when_no_next_review_phrase_present_should_return_empty_optional() {
//        //use mock bean repository and delete all records
//        toBeReviewedRepository.deleteAll();
//        //test that the getReviewPhrase() method returns an empty Optional object
//        assertFalse(toBeReviewedService.getReviewPhrase().isPresent());

        when(toBeReviewedService.getReviewPhrase().thenReturn()

    }
}
