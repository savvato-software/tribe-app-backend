package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@IdClass(ReviewDecisionId.class)
public class ReviewDecision {
    @Id
    public Long reviewId;
    @Id
    public Long userId;
    public Long reasonId;

    public ReviewDecision(Long reviewId, Long userId, Long reasonId) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.reasonId = reasonId;
    }

    public ReviewDecision() {
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getReasonId() {
        return reasonId;
    }

    public void setReasonId(Long reasonId) {
        this.reasonId = reasonId;
    }
}
