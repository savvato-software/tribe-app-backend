package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class ConnectOutgoingMessageDTOUpdated {
    public Boolean connectionError;

    public Boolean connectionSuccess;

    public String  message;

    public Long to;
}
