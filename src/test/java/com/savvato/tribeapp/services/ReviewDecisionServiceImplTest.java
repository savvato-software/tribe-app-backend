package com.savvato.tribeapp.services;

import com.savvato.tribeapp.controllers.dto.ReviewDecisionRequest;
import com.savvato.tribeapp.entities.ReviewDecision;
import com.savvato.tribeapp.entities.ReviewDecisionReason;
import com.savvato.tribeapp.repositories.ReviewDecisionReasonRepository;
import com.savvato.tribeapp.repositories.ReviewDecisionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
    public void saveReviewDecision() {
        ReviewDecisionRequest reviewDecisionRequest = new ReviewDecisionRequest();
        reviewDecisionRequest.reviewId = 1L;
        reviewDecisionRequest.reviewerId = 2L;
        reviewDecisionRequest.decision = "approved";

        ReviewDecisionReason reviewDecisionReason = new ReviewDecisionReason();
        reviewDecisionReason.setId(1L);
        reviewDecisionReason.setReason("approved");
        Mockito.when(reviewDecisionReasonRepository.findByReason(Mockito.any())).thenReturn(Optional.of(reviewDecisionReason));

        reviewDecisionService.saveReviewDecision(reviewDecisionRequest.reviewId, reviewDecisionRequest.reviewerId, reviewDecisionRequest.decision);

        ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
        verify(reviewDecisionReasonRepository, times(1)).findByReason(arg1.capture());
        assertEquals(arg1.getValue(), reviewDecisionReason.getReason());
    }
}
