package com.savvato.tribeapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.savvato.tribeapp.models.NotificationType;
import com.savvato.tribeapp.models.Notification;
import com.savvato.tribeapp.repositories.NotificationRepository;
import com.savvato.tribeapp.repositories.NotificationTypeRepository;
import com.savvato.tribeapp.dto.NotificationDTO;




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
            return new NotificationDTO(notification.get().getDescription(),
                    notification.get().getBody(),
                    notification.get().getLastUpdatedDate(),
                    iconUrl);
        }
        return null;
    }
}
