package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder

public class NotificationDTO {
    public String description;
    public String body;
    public String lastUpdatedDate;
    public String iconUrl;

    public NotificationDTO(String description, String body, String lastUpdatedDate, String iconUrl) {
        this.description = description;
        this.body = body;
        this.lastUpdatedDate = lastUpdatedDate;
        this.iconUrl = iconUrl;
    }
}
