package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.NotificationDTO;
import com.savvato.tribeapp.entities.Notification;
import com.savvato.tribeapp.entities.NotificationType;

public interface NotificationService {

    public NotificationDTO getNotificationDTOById(Long id);

    public boolean checkNotificationReadStatus(Long id);

    public boolean updateNotificationReadStatus(Long id);

    public Notification createNotification(NotificationType type, Long userId, String description, String body);

}
