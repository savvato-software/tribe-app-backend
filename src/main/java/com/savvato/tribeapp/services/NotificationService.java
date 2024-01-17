package com.savvato.tribeapp.services;

import com.savvato.tribeapp.dto.GenericMessageDTO;
import com.savvato.tribeapp.dto.NotificationDTO;
import com.savvato.tribeapp.entities.Notification;
import com.savvato.tribeapp.entities.NotificationType;
import java.util.List;

public interface NotificationService {

    public List<NotificationDTO> getUserNotifications(Long userId);

    public NotificationDTO createNotificationDTO(Notification notification, String formattedLastUpdatedDate , String iconUrl);


    public boolean checkNotificationReadStatus(Long id);

    public boolean updateNotificationReadStatus(Long id);

    public Notification createNotification(NotificationType type, Long userId, String description, String body);

    void deleteNotification(Long id);

    boolean checkNotificationExists(Long id);

    List<Notification> getNotificationsByUserId(Long userid);

    String getIconUrlFromNotification(Notification notification);

    String getFormattedLastUpdatedDate(Notification notification);
}
