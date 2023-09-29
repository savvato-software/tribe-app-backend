package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import com.savvato.tribeapp.entities.ReviewDecisionReason;
import com.savvato.tribeapp.repositories.ReviewDecisionReasonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)

public class ReviewDecisionReasonServiceImplTest {
    @TestConfiguration
    static class ReviewDecisionReasonServiceTestContextConfiguration {
        @Bean
        public ReviewDecisionReasonService reviewDecisionReasonService() {
            return new ReviewDecisionReasonServiceImpl();
        }
    }

    @Autowired
    ReviewDecisionReasonService reviewDecisionReasonService;

    @MockBean
    ReviewDecisionReasonRepository reviewDecisionReasonRepository;

    @Test
    public void getReviewDecisionReasonsHappyPath() {

        List<ReviewDecisionReason> rdrList = new ArrayList<>();

        for(int i=0; i<5; i++) {
            ReviewDecisionReason rdr = new ReviewDecisionReason();
            rdr.setId(Integer.toUnsignedLong(i+1));
            rdr.setReason("testing" + i);
            rdrList.add(rdr);
        }

        Mockito.when(reviewDecisionReasonRepository.findAllReviewDecisionReasons()).thenReturn(rdrList);
        List<ReviewDecisionReasonDTO> rdrDtoList = reviewDecisionReasonService.getReviewDecisionReasons();
        assertEquals(rdrDtoList.size(), 5);
        assertEquals(rdrDtoList.get(4).id, 5L);
        assertEquals(rdrDtoList.get(4).reason, "testing4");

    }

    @Test
    public void getReviewDecisionReasonsSadPath() {
        List emptyList = new ArrayList<>();
        Mockito.when(reviewDecisionReasonRepository.findAllReviewDecisionReasons()).thenReturn(emptyList);
        assertThrows(IllegalStateException.class, () -> {
            reviewDecisionReasonService.getReviewDecisionReasons();
        });

    }
}
