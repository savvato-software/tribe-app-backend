package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class NotificationUpdateDTO {
    public long id;
    public boolean isRead;
}
