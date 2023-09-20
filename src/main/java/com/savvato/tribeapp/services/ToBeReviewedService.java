package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.ToBeReviewedDTO;
import com.savvato.tribeapp.entities.ToBeReviewed;

import java.util.Optional;

public interface ToBeReviewedService {
    Optional<ToBeReviewedDTO> getReviewPhrase();

    void setLastAssignedForReview(Long id);

    Long getLastAssignedForReview();
}
