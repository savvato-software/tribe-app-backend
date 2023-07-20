package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@Table(name="to_be_reviewed")
public class ToBeReviewed {
    public Long getId() {
        return id;
    }
    public ToBeReviewed(Long id, Boolean hasBeenGroomed, String adverb, String verb, String preposition, String noun) {
        this.id = id;
        this.hasBeenGroomed = hasBeenGroomed;
        this.adverb = adverb;
        this.verb = verb;
        this.preposition = preposition;
        this.noun = noun;
    }

    public ToBeReviewed() {
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
    @Override
    public String toString() {
        String rtn = (this.getAdverb() != "") ? this.getAdverb() + " " + this.getVerb() : this.getVerb();
        rtn += " " + ((this.getPreposition() != "") ? this.getPreposition() + " " + this.getNoun() : this.getNoun());
        return rtn;
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
