package com.savvato.tribeapp.services;
import com.savvato.tribeapp.dto.ToBeReviewedDTO;
import com.savvato.tribeapp.entities.*;
import com.savvato.tribeapp.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class ReviewSubmittingUserServiceImplTest extends
        AbstractServiceImplTest {

    @TestConfiguration
    static class ReviewSubmittingUserServiceTestContextConfiguration {

        @Bean
        public ReviewSubmittingUserService reviewSubmittingUserService() {
            return new ReviewSubmittingUserServiceImpl();
        }
    }

    @Autowired
    ReviewSubmittingUserService reviewSubmittingUserService;

    @MockBean
    ToBeReviewedRepository toBeReviewedRepository;

    @MockBean
    ReviewSubmittingUserRepository reviewSubmittingUserRepository;

    // Test that the method returns the expected DTO versions of the ToBeReviewed phrases associated with a user
    @Test
    public void testGetUserPhrasesToBeReviewedHappyPath() {
        List<Long> toBeReviewedIds = new ArrayList<>(Arrays.asList(1L, 2L));
        Mockito.when(reviewSubmittingUserRepository.findToBeReviewedIdByUserId(anyLong
                ())).thenReturn(toBeReviewedIds);

        String testWord = "test";
        List<ToBeReviewed> tbrs = new ArrayList<>();
        for (Long toBeReviewedId : toBeReviewedIds) {
            ToBeReviewed tbr = new ToBeReviewed();
            tbr.setId(toBeReviewedId);
            tbr.setHasBeenGroomed(true);
            tbr.setAdverb(testWord);
            tbr.setVerb(testWord);
            tbr.setPreposition(testWord);
            tbr.setNoun(testWord);
            tbrs.add(tbr);
        }

        Mockito.when(toBeReviewedRepository.findById(anyLong()))
                .thenReturn(Optional.of(tbrs.get(0)))
                .thenReturn(Optional.of(tbrs.get(1)));

        List<ToBeReviewedDTO> expectedDTOs = new ArrayList<>();
        for (ToBeReviewed tbr : tbrs) {
            ToBeReviewedDTO tbrDTO = ToBeReviewedDTO.builder()
                    .id(tbr.getId())
                    .hasBeenGroomed(tbr.isHasBeenGroomed())
                    .adverb(tbr.getAdverb())
                    .verb(tbr.getVerb())
                    .preposition(tbr.getPreposition())
                    .noun(tbr.getNoun())
                    .build();
            expectedDTOs.add(tbrDTO);
        }

        List<ToBeReviewedDTO> rtnDTOs = reviewSubmittingUserService.getUserPhrasesToBeReviewed(1L);
        for (ToBeReviewedDTO rtnDTO : rtnDTOs) {
            for (ToBeReviewedDTO expectedDTO : expectedDTOs) {
                assertEquals(rtnDTO.hasBeenGroomed, expectedDTO.hasBeenGroomed);
                assertEquals(rtnDTO.adverb, expectedDTO.adverb);
                assertEquals(rtnDTO.verb, expectedDTO.verb);
                assertEquals(rtnDTO.preposition, expectedDTO.preposition);
                assertEquals(rtnDTO.noun, expectedDTO.noun);
            }
        }
    }
}