package com.savvato.tribeapp.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private NotificationType type;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "isRead", nullable = false)
    private boolean isRead;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_updated_date", nullable = false)
    private LocalDateTime lastUpdatedDate;

    // getters and setters
    // equals and hashCode methods
    // toString method
}
