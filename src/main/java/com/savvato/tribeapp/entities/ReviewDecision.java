package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@IdClass(ReviewDecisionId.class)
public class ReviewDecision {
    @Id
    public Long reviewId;
    @Id
    public Long userId;
    public Long reviewDecisionReasonId;

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

    public Long getReviewDecisionReasonId() {
        return reviewDecisionReasonId;
    }

    public void setReviewDecisionReasonId(Long reviewDecisionReasonId) {
        this.reviewDecisionReasonId = reviewDecisionReasonId;
    }
}
