package com.savvato.tribeapp.entities;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@IdClass(ReviewDecisionId.class)
public class ReviewDecision {
    @Id
    public Long reviewId;
    @Id
    public Long userId;
    public Long reasonId;
    private java.sql.Timestamp created;


    public ReviewDecision(Long reviewId, Long userId, Long reasonId) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.reasonId = reasonId;
        setCreated();
    }

<<<<<<< HEAD
    public ReviewDecision() {
    }

=======
>>>>>>> trib-59-test
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
    public java.sql.Timestamp getCreated() {
        return created;
    }

    public void setCreated() {
        this.created = java.sql.Timestamp.from(Calendar.getInstance().toInstant());
    }
}
