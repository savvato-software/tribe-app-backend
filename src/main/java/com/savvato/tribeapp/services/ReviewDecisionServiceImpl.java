package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.ReviewDecision;
import com.savvato.tribeapp.entities.ReviewDecisionReason;
import com.savvato.tribeapp.repositories.ReviewDecisionReasonRepository;
import com.savvato.tribeapp.repositories.ReviewDecisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewDecisionServiceImpl implements ReviewDecisionService {
    @Autowired
    ReviewDecisionRepository reviewDecisionRepository;

    @Autowired
    ReviewDecisionReasonRepository reviewDecisionReasonRepository;
    @Override
    public ReviewDecision saveReviewDecision(Long reviewId, Long userId, Long reasonId) {
        ReviewDecision decision = new ReviewDecision(reviewId, userId, reasonId);
        ReviewDecision newReviewDecisionRecord = reviewDecisionRepository.save(decision);
        return newReviewDecisionRecord;
    }

}
