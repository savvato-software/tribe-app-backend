package com.savvato.tribeapp.entities;

import java.io.Serializable;

public class CosignId implements Serializable {
    private Long userIdIssuing;
    private Long userIdReceiving;
    private Long phraseId;

    public Long getUserIdIssuing() { return userIdIssuing; }

    public void setUserIdIssuing(Long userIdIssuing) { this.userIdIssuing = userIdIssuing; }

    public Long getUserIdReceiving() { return userIdReceiving; }

    public void setUserIdReceiving(Long userIdReceiving) { this.userIdReceiving = userIdReceiving; }

    public Long getPhraseId() { return phraseId; }

    public void setPhraseId(Long phraseId) { this.phraseId = phraseId; }
}
