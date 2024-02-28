package com.savvato.tribeapp.entities;

import java.io.Serializable;


public class ConnectionId implements Serializable {
    public Long requestingUserId;
    public Long toBeConnectedWithUserId;
}
