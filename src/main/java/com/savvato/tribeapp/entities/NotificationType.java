package com.savvato.tribeapp.models;

import javax.persistence.*;

@Entity
@Table(name = "notification_type")
public class NotificationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon_url", nullable = false)
    private String iconUrl;

    // getters and setters
    // equals and hashCode methods
    // toString method
}
