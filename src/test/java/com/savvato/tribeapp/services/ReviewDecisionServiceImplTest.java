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
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        ReviewDecision decision = new ReviewDecision(1L, 1L, 1L);
        Mockito.when(reviewDecisionRepository.save(Mockito.any())).thenReturn(decision);
        ReviewDecision saveResult = reviewDecisionService.saveReviewDecision(decision.getReviewId(), decision.getUserId(), decision.getReasonId());

        // Ensure that save() is called at least once on reviewDecisionRepository,
        //  and that the arguments passed to save() match the given decision
        ArgumentCaptor<ReviewDecision> arg1 = ArgumentCaptor.forClass(ReviewDecision.class);
        verify(reviewDecisionRepository, times(1)).save(arg1.capture());
        assertEquals(arg1.getValue().getReviewId(), decision.getReviewId());
        assertEquals(arg1.getValue().getUserId(), decision.getUserId());
        assertEquals(arg1.getValue().getReasonId(), decision.getReasonId());

        // Ensure that the result returned by the save() function matches the given decision
        assertEquals(saveResult.getReviewId(), decision.getReviewId());
        assertEquals(saveResult.getUserId(), decision.getUserId());
        assertEquals(saveResult.getReasonId(), decision.getReasonId());
    }
}
