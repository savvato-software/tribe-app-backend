package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@Table(name="connections")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestingUserId;
    private Long toBeConnectedWithUserId;
    private String qrcodePhrase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRequestingUserId() {
        return requestingUserId;
    }

    public void setRequestingUserId(Long requestingUserId) {
        this.requestingUserId = requestingUserId;
    }

    public Long getToBeConnectedWithUserId() {
        return toBeConnectedWithUserId;
    }

    public void setToBeConnectedWithUserId(Long toBeConnectedWithUserId) {
        this.toBeConnectedWithUserId = toBeConnectedWithUserId;
    }

    public String getQrcodePhrase() {
        return qrcodePhrase;
    }

    public void setQrcodePhrase(String qrcodePhrase) {
        this.qrcodePhrase = qrcodePhrase;
    }

    public Connection(Long id, Long requestingUserId, Long toBeConnectedWithUserId, String qrcodePhrase) {
        this.id = id;
        this.requestingUserId = requestingUserId;
        this.toBeConnectedWithUserId = toBeConnectedWithUserId;
        this.qrcodePhrase = qrcodePhrase;
    }
    public Connection() {

    }
}
