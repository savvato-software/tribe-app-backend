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

    public int adverbId;

    public int verbId;

    public int prepositionId;

    public int nounID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAdverbId() {
        return adverbId;
    }

    public void setAdverbId(int adverbId) {
        this.adverbId = adverbId;
    }

    public int getVerbId() {
        return verbId;
    }

    public void setVerbId(int verbId) {
        this.verbId = verbId;
    }

    public int getPrepositionId() {
        return prepositionId;
    }

    public void setPrepositionId(int prepositionId) {
        this.prepositionId = prepositionId;
    }

    public int getNounID() {
        return nounID;
    }

    public void setNounID(int nounID) {
        this.nounID = nounID;
    }
}

