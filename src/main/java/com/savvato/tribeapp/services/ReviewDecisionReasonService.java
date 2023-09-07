package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReviewDecisionReasonService {

    Optional<List<ReviewDecisionReasonDTO>> getReviewDecisionReasons();
}
