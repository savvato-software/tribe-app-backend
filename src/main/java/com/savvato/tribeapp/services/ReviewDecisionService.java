package com.savvato.tribeapp.services;
import com.savvato.tribeapp.entities.ReviewDecision;
import org.springframework.stereotype.Service;

@Service
public interface ReviewDecisionService {
    ReviewDecision saveReviewDecision(Long reviewId, Long userId, Long reasonId);
}
