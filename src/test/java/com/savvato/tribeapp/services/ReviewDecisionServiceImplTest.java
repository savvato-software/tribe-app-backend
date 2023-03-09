package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.ReviewDecisionRequest;
import com.savvato.tribeapp.entities.ReviewDecision;
import com.savvato.tribeapp.entities.ReviewDecisionReason;
import com.savvato.tribeapp.repositories.ReviewDecisionReasonRepository;
import com.savvato.tribeapp.repositories.ReviewDecisionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)

public class ReviewDecisionServiceImplTest {
    @TestConfiguration
    static class ReviewDecisionServiceTestContextConfiguration {
        @Bean
        public ReviewDecisionService reviewDecisionService() {
            return new ReviewDecisionServiceImpl();
        }
    }

    @Autowired
    ReviewDecisionService reviewDecisionService;

    @MockBean
    ReviewDecisionRepository reviewDecisionRepository;
    @MockBean
    ReviewDecisionReasonRepository reviewDecisionReasonRepository;

    @Test
    public void getReviewDecisionReasonIdHappyPath() {
        String validDecision = "approved";
        ReviewDecisionReason reviewDecisionReason = new ReviewDecisionReason();
        reviewDecisionReason.setId(1L);
        reviewDecisionReason.setReason("approved");
        Mockito.when(reviewDecisionReasonRepository.findByReason(Mockito.any())).thenReturn(Optional.of(reviewDecisionReason));

        Long reviewDecisionReasonId = reviewDecisionService.getReviewDecisionReasonId(validDecision);
        assertEquals(reviewDecisionReasonId, reviewDecisionReason.getId());

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        verify(reviewDecisionReasonRepository, times(1)).findByReason(arg1.capture());
        assertEquals(arg1.getValue(), reviewDecisionReason.getReason());
    }

    @Test
    public void getReviewDecisionReasonIdWhenDecisionInvalid() {
        String invalidDecision = "invalid_decision";
        Mockito.when(reviewDecisionReasonRepository.findByReason(Mockito.any())).thenReturn(Optional.empty());

        Long reviewDecisionReasonId = reviewDecisionService.getReviewDecisionReasonId(invalidDecision);
        assertEquals(reviewDecisionReasonId, -1L);

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        verify(reviewDecisionReasonRepository, times(1)).findByReason(arg1.capture());
        assertEquals(arg1.getValue(), invalidDecision);
    }

    @Test
    public void saveReviewDecisionHappyPath() {
        ReviewDecisionRequest reviewDecisionRequest = new ReviewDecisionRequest();
        reviewDecisionRequest.reviewId = 1L;
        reviewDecisionRequest.reviewerId = 2L;
        reviewDecisionRequest.decision = "approved";

        ReviewDecisionReason reviewDecisionReason = new ReviewDecisionReason();
        reviewDecisionReason.setId(1L);
        reviewDecisionReason.setReason("approved");

        Mockito.when(reviewDecisionReasonRepository.findByReason(Mockito.any())).thenReturn(Optional.of(reviewDecisionReason));
        boolean rtn = reviewDecisionService.saveReviewDecision(reviewDecisionRequest.reviewId, reviewDecisionRequest.reviewerId, reviewDecisionRequest.decision);

        ArgumentCaptor<ReviewDecision> arg1 = ArgumentCaptor.forClass(ReviewDecision.class);
        verify(reviewDecisionRepository, times(1)).save(arg1.capture());
        assertEquals(arg1.getValue().reviewId, reviewDecisionRequest.reviewId);
        assertEquals(arg1.getValue().userId, reviewDecisionRequest.reviewerId);
        assertEquals(arg1.getValue().reviewDecisionReasonId, reviewDecisionReason.getId());
        assertThat(rtn).isTrue();
    }

    @Test
    public void saveReviewDecisionWhenDecisionInvalid() {
        ReviewDecisionRequest reviewDecisionRequest = new ReviewDecisionRequest();
        reviewDecisionRequest.reviewId = 1L;
        reviewDecisionRequest.reviewerId = 2L;
        reviewDecisionRequest.decision = "invalid_decision";

        Mockito.when(reviewDecisionReasonRepository.findByReason(Mockito.any())).thenReturn(Optional.empty());
        boolean rtn = reviewDecisionService.saveReviewDecision(reviewDecisionRequest.reviewId, reviewDecisionRequest.reviewerId, reviewDecisionRequest.decision);

        ArgumentCaptor<ReviewDecision> arg1 = ArgumentCaptor.forClass(ReviewDecision.class);
        verify(reviewDecisionRepository, times(0)).save(arg1.capture());
        assertThat(rtn).isFalse();
    }

    // TODO: Confirm no need to test for reviewerId, reviewId being invalid since those should be automatically generated
    // TODO: Confirm length of test names
}
