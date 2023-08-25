package com.savvato.tribeapp.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class ConnectOutgoingMessageDTO {
    public Boolean connectionError;

    public Boolean connectionSuccess;

    public String  message;

    public List<Long> to;
}
