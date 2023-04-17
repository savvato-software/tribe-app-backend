package com.savvato.tribeapp.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(UserPhraseId.class)
public class UserPhrase {

    @Id
    public Long userId;

    @Id
    public Long phraseId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(Long phraseId) {
        this.phraseId = phraseId;
    }
}
