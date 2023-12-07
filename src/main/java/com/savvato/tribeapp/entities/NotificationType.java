package com.savvato.tribeapp.entities;

import javax.persistence.*;

@Entity
@Table(name = "notification_type")
public class NotificationType {

    public static final NotificationType ATTRIBUTE_REQUEST_APPROVED = new NotificationType(1L, "Attribute request approved", null);
    public static final NotificationType ATTRIBUTE_REQUEST_REJECTED = new NotificationType(1L, "Attribute request rejected", null);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon_url", nullable = false)
    private String iconUrl;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public NotificationType() {

    }

    public NotificationType(Long id, String name, String iconUrl) {
    	this.id = id;
    	this.name = name;
    	this.iconUrl = iconUrl;
    }
}
