package com.savvato.tribeapp.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@IdClass(ConnectionId.class)
@Table(name = "connections")
public class Connection {
    @Id
    private Long requestingUserId;
    @Id
    private Long toBeConnectedWithUserId;
    private java.sql.Timestamp createdTimestamp;

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
        return createdTimestamp;
    }

    public void setCreated() {
        this.createdTimestamp = java.sql.Timestamp.from(Calendar.getInstance().toInstant());
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
