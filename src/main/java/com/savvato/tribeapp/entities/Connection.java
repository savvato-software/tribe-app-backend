package com.savvato.tribeapp.entities;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name="connections")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestingUserId;
    private Long toBeConnectedWithUserId;
    private java.sql.Timestamp created;
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

    public java.sql.Timestamp getCreated() {
        return created;
    }

    public void setCreated() {
        this.created = java.sql.Timestamp.from(Calendar.getInstance().toInstant());
    }

    public Connection(Long requestingUserId, Long toBeConnectedWithUserId) {
        this.requestingUserId = requestingUserId;
        this.toBeConnectedWithUserId = toBeConnectedWithUserId;

        setCreated();
    }
    public Connection() {

        setCreated();

    }
}
