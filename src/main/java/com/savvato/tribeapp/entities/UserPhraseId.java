package com.savvato.tribeapp.entities;
import java.io.Serializable;

public class UserPhraseId implements Serializable {
    private Long userId;
    private Long phraseId;

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