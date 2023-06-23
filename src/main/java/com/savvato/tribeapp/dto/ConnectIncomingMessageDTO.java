package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class ConnectIncomingMessageDTO {
    public Long requestingUserId;
    public Long toBeConnectedWithUserId;

    public String qrcodePhrase;

    public String connectionIntent;
}
