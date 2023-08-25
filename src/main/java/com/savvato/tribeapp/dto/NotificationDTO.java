package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class NotificationDTO {
    public long id;
    public String description;
    public String body;
    public String lastUpdatedDate;
    public String iconUrl;
    public boolean isRead;
}
