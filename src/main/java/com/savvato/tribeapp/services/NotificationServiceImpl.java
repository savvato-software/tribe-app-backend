package com.savvato.tribeapp.services;

import com.savvato.tribeapp.entities.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.savvato.tribeapp.entities.Notification;
import com.savvato.tribeapp.repositories.NotificationRepository;
import com.savvato.tribeapp.repositories.NotificationTypeRepository;
import com.savvato.tribeapp.dto.NotificationDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.Duration;


@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    public NotificationDTO createNotificationDTO(Notification notification) {
        String iconUrl = getIconUrlFromNotification(notification);
        String formattedLastUpdatedDate = getFormattedLastUpdatedDate(notification);

        return NotificationDTO.builder()
                .description(notification.getDescription())
                .body(notification.getBody())
                .lastUpdatedDate(formattedLastUpdatedDate)
                .iconUrl(iconUrl)
                .build();
    }

    public String getIconUrlFromNotification(Notification notification) {
        NotificationType type = notification.getType();
        return type != null ? type.getIconUrl() : null;
    }

    public String getFormattedLastUpdatedDate(Notification notification) {
        Instant lastUpdatedInstant = notification.getLastUpdatedDate().atZone(ZoneOffset.UTC).toInstant();
        Instant currentInstant = Instant.now();
        long ageInMilliseconds = Duration.between(lastUpdatedInstant, currentInstant).toMillis();
        return String.valueOf(ageInMilliseconds);
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
    public Notification createNotification(NotificationType type, Long userId, String description, String body) {
        Optional<NotificationType> optionalType = notificationTypeRepository.findById(type.getId());

        if (optionalType.isPresent()) {
            NotificationType notificationType = optionalType.get();

            Notification notification = new Notification();
            notification.setType(notificationType);
            notification.setUserId(userId);
            notification.setDescription(description);
            notification.setBody(body);
            notification.setRead(false);
            notification.setCreatedDate(LocalDateTime.now());
            notification.setLastUpdatedDate(LocalDateTime.now());

            return notificationRepository.save(notification);
        }

        return null;
    }
    public void deleteNotification(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notificationRepository.delete(notification);
        }
    }

    public boolean checkNotificationExists(Long id) {
        return notificationRepository.existsById(id);
    }
    //tested
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public boolean checkNotificationExists(Long id) {
        return notificationRepository.existsById(id);
    }
}
