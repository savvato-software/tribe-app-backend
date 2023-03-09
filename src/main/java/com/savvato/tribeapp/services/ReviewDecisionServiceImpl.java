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
    public boolean saveReviewDecision(Long reviewId, Long userId, String reason) {
        Long reasonId = getReviewDecisionReasonId(reason);
        if (reasonId != -1L) {
            ReviewDecision decision = new ReviewDecision();
            decision.setReviewId(reviewId);
            decision.setUserId(userId);
            decision.setReviewDecisionReasonId(reasonId);

            reviewDecisionRepository.save(decision);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Long getReviewDecisionReasonId(String reason) {
        Optional<ReviewDecisionReason> opt = reviewDecisionReasonRepository.findByReason(reason);
        if (opt.isPresent()) {
            return opt.get().getId();
        }
        return -1L;
    }
}
