package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(targetEntity = Adverb.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "adverbId")
    public Long adverbId;

//    @ManyToOne(targetEntity = Verb.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "verbId")
    public Long verbId;

//    @ManyToOne(targetEntity = Preposition.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "prepositionId")
    public Long prepositionId;

//    @ManyToOne(targetEntity = Noun.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "nounId")
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

    public Long getNounId() {
        return nounID;
    }

    public void setNounId(Long nounID) {
        this.nounID = nounID;
    }
}