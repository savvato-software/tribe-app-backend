package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="noun")
public class Noun extends AbstractWordEntity {

    public Noun(String word) {
        this.setWord(word);
    }

    public Noun(long id, String word) {
        this.setId(id);
        this.setWord(word);
    }

    public Noun() {

    }
}
