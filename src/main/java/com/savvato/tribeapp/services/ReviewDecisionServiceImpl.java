package com.savvato.tribeapp.services;

import com.plivo.api.xml.P;
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
    public boolean saveReviewDecision(Long reviewId, Long reviewerId, String reason) {
        Optional<ReviewDecisionReason> opt = reviewDecisionReasonRepository.findByReason(reason);
        if (opt.isPresent()) {
            return true;
        }
        else {
            return false;
        }
    }
}
