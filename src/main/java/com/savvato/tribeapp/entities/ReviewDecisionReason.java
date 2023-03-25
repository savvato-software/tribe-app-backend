package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@Table(name="review_decision_reason")
public class ReviewDecisionReason {
    public ReviewDecisionReason() {
    }
    public ReviewDecisionReason(Long id, String reason) {
        this.id = id;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String reason;

}
