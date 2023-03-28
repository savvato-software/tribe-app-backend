package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="verb")
public class Verb extends AbstractWordEntity {

    public Verb(String word) {
        this.setWord(word);
    }

    public Verb(long id, String word) {
        this.setId(id);
        this.setWord(word);
    }

    public Verb() {

    }
}
