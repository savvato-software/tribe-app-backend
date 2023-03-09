package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ReviewDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long reviewId;
    public Long userId;
    public Long reviewDecisionReasonId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getReviewDecisionReasonId() {
        return reviewDecisionReasonId;
    }

    public void setReviewDecisionReasonId(Long reviewDecisionReasonId) {
        this.reviewDecisionReasonId = reviewDecisionReasonId;
    }
}
