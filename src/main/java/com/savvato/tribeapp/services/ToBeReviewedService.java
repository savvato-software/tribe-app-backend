package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.ToBeReviewed;

import java.util.Optional;

public interface ToBeReviewedService {
    Optional<ToBeReviewed> getReviewPhraseWithoutPlaceholderNullvalue();

    void setLastAssignedForReview(Long id);

    Long getLastAssignedForReview();
}
