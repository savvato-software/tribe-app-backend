package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@Table(name="rejected_non_english_word")
public class RejectedNonEnglishWord extends AbstractWordEntity {

    public RejectedNonEnglishWord(String word) {
        this.setWord(word);
    }

    public RejectedNonEnglishWord(long id, String word) {
        this.setId(id);
        this.setWord(word);
    }

    public RejectedNonEnglishWord() {

    }
}
