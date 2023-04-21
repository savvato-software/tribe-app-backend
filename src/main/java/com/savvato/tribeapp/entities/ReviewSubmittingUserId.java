package com.savvato.tribeapp.entities;

import java.io.Serializable;

public class ReviewSubmittingUserId implements Serializable {
    private Long userId;
    private Long toBeReviewedId;

    public ReviewSubmittingUserId(Long userId, Long toBeReviewedId) {
        this.userId = userId;
        this.toBeReviewedId = toBeReviewedId;
    }
}
