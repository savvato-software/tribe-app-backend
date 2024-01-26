package com.savvato.tribeapp.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long adverbId;

    public Long verbId;

    public Long prepositionId;

    public Long nounId;

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
        return nounId;
    }

    public void setNounId(Long nounID) {
        this.nounId = nounID;
    }

    @OneToMany
    @JoinTable(
            name = "user_phrase",
            joinColumns = {@JoinColumn(name = "phraseId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")})
    private List<User> users;


}
