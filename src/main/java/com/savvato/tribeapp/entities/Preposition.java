package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="preposition")
public class Preposition extends AbstractWordEntity {

    public Preposition(String word) {
        this.setWord(word);
    }

    public Preposition(long id, String word) {
        this.setId(id);
        this.setWord(word);
    }

    public Preposition() {

    }
}
