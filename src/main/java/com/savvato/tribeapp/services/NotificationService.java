package com.savvato.tribeapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.savvato.tribeapp.models.NotificationType;
import com.savvato.tribeapp.models.Notification;
import com.savvato.tribeapp.repositories.NotificationRepository;
import com.savvato.tribeapp.repositories.NotificationTypeRepository;
import com.savvato.tribeapp.dto.NotificationDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;


    public NotificationDTO getNotificationDTOById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if (notification.isPresent()) {
            NotificationType type = notification.get().getType();
            String iconUrl = type != null ? type.getIconUrl() : null;

            // Code to find ms since last update (not working)
            Instant instant = notification.get().getLastUpdatedDate().atZone(ZoneOffset.UTC).toInstant();
            LocalDateTime lastUpdatedDate = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

            return new NotificationDTO(notification.get().getDescription(),
                    notification.get().getBody(),
                    lastUpdatedDate,  // Replace with formatted lastUpdatedDate
                    iconUrl);
        }
        return null;
    }
}