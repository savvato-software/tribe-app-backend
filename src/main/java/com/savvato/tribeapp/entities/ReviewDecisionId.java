package com.savvato.tribeapp.entities;

import java.io.Serializable;

public class ReviewDecisionId implements Serializable {
    private Long reviewId;
    private Long userId;

    public ReviewDecisionId(Long reviewId, Long userId) {
        this.reviewId = reviewId;
        this.userId = userId;
    }
}
