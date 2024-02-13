package com.savvato.tribeapp.entities;

import lombok.Generated;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "connections")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long requestingUserId;
    private Long toBeConnectedWithUserId;
    private java.sql.Timestamp created;
<<<<<<< HEAD

    @Generated
=======
>>>>>>> feature/connect-page
    public Long getId() {
        return id;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public Long getRequestingUserId() {
        return requestingUserId;
    }

    @Generated
    public void setRequestingUserId(Long requestingUserId) {
        this.requestingUserId = requestingUserId;
    }

    @Generated
    public Long getToBeConnectedWithUserId() {
        return toBeConnectedWithUserId;
    }

    @Generated
    public void setToBeConnectedWithUserId(Long toBeConnectedWithUserId) {
        this.toBeConnectedWithUserId = toBeConnectedWithUserId;
    }

    @Generated
    public java.sql.Timestamp getCreated() {
        return created;
    }

    @Generated
    public void setCreated() {
        this.created = java.sql.Timestamp.from(Calendar.getInstance().toInstant());
    }

    public Connection(Long requestingUserId, Long toBeConnectedWithUserId) {
        this.requestingUserId = requestingUserId;
        this.toBeConnectedWithUserId = toBeConnectedWithUserId;

        setCreated();
    }

    @Generated
    public Connection() {

        setCreated();

    }
}
