package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="adverb")
public class Adverb extends AbstractWordEntity {

    public Adverb(String word) {
        this.setWord(word);
    }

    public Adverb(long id, String word) {
        this.setId(id);
        this.setWord(word);
    }

    public Adverb() {

    }
}
