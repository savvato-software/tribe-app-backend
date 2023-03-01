package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@Table(name="to_be_reviewed")
public class Phrase {
    public Long getId() {
        return id;
    }

    public Boolean isHasBeenGroomed() {
        return hasBeenGroomed;
    }

    public String getAdverb() {
        return adverb;
    }

    public String getVerb() {
        return verb;
    }

    public String getPreposition() {
        return preposition;
    }

    public String getNoun() {
        return noun;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHasBeenGroomed(Boolean hasBeenGroomed) {
        this.hasBeenGroomed = hasBeenGroomed;
    }

    public void setAdverb(String adverb) {
        this.adverb = adverb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public void setPreposition(String preposition) {
        this.preposition = preposition;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean hasBeenGroomed;
    private String adverb;
    private String verb;
    private String preposition;
    private String noun;
}
