package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class ConnectOutgoingMessageDTO {
    public Boolean connectionError;

    public Boolean connectionSuccess;

    public String  message;

    public Long to;
}
