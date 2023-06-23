package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.savvato.tribeapp.models.Notification;
import com.savvato.tribeapp.repositories.NotificationRepository;
import com.savvato.tribeapp.repositories.NotificationTypeRepository;
import com.savvato.tribeapp.dto.NotificationDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.Duration;


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

            Instant lastUpdatedInstant = notification.get().getLastUpdatedDate().atZone(ZoneOffset.UTC).toInstant();
            Instant currentInstant = Instant.now();
            long ageInMilliseconds = Duration.between(lastUpdatedInstant, currentInstant).toMillis();

            String formattedLastUpdatedDate = String.valueOf(ageInMilliseconds);

            return new NotificationDTO(notification.get().getDescription(),
                    notification.get().getBody(),
                    formattedLastUpdatedDate,
                    iconUrl);
        }
        return null;
    }

    public boolean checkNotificationReadStatus(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        return optionalNotification.map(Notification::isRead).orElse(false);
    }

    public boolean updateNotificationReadStatus(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setRead(true);
            notificationRepository.save(notification);
            return true;
        }
        return false;
    }
}
