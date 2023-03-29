package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@Table(name="preposition")
public class Preposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

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