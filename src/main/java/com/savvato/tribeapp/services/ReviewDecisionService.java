package com.savvato.tribeapp.services;

import org.springframework.stereotype.Service;

@Service
public interface ReviewDecisionService {
    boolean saveReviewDecision(Long reviewId, Long reviewerId, String reason);
}
