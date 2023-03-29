package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long adverbId;

    public Long verbId;

    public Long prepositionId;

    public Long nounID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdverbId() {
        return adverbId;
    }

    public void setAdverbId(Long adverbId) {
        this.adverbId = adverbId;
    }

    public Long getVerbId() {
        return verbId;
    }

    public void setVerbId(Long verbId) {
        this.verbId = verbId;
    }

    public Long getPrepositionId() {
        return prepositionId;
    }

    public void setPrepositionId(Long prepositionId) {
        this.prepositionId = prepositionId;
    }

    public Long getNounID() {
        return nounID;
    }

    public void setNounID(Long nounID) {
        this.nounID = nounID;
    }
}