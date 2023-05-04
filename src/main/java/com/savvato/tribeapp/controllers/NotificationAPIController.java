package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.dto.NotificationDTO;
import com.savvato.tribeapp.models.Notification;
import com.savvato.tribeapp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@RequestMapping("/notifications")
public class NotificationAPIController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        NotificationDTO notification = notificationService.getNotificationDTOById(id);
        return notification != null ? ResponseEntity.ok(notification) : ResponseEntity.notFound().build();
    }

}
