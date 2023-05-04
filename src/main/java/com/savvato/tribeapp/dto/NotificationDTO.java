package com.savvato.tribeapp.dto;

import java.time.LocalDateTime;


public class NotificationDTO {
    private String description;
    private String body;
    private LocalDateTime lastUpdatedDate;
    private String iconUrl;

    public NotificationDTO(String description, String body, LocalDateTime lastUpdatedDate, String iconUrl) {
        this.description = description;
        this.body = body;
        this.lastUpdatedDate = lastUpdatedDate;
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
// getters and setters
}
