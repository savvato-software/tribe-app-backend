package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ReviewDecisionReasonService {

    List<ReviewDecisionReasonDTO> getReviewDecisionReasons();
}
