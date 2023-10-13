package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.NotificationRequest;
import com.savvato.tribeapp.dto.NotificationDTO;
import com.savvato.tribeapp.dto.GenericMessageDTO;
import com.savvato.tribeapp.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/notifications")
public class NotificationAPIController {

    @Autowired
    private NotificationService notificationService;

    @PutMapping
    public ResponseEntity<String> updateNotification(@RequestBody @Valid NotificationRequest req) {
        boolean isRead = notificationService.checkNotificationReadStatus(req.id);
        if (isRead) {
            return ResponseEntity.ok("Notification is already read");
        } else {
            notificationService.updateNotificationReadStatus(req.id);
            return ResponseEntity.ok("Notification read status updated");
        }
    }
    @DeleteMapping("/{id}")
    public GenericMessageDTO deleteNotification(@PathVariable Long id) {
        boolean exists = notificationService.checkNotificationExists(id);
        if (exists) {
            notificationService.deleteNotification(id);
            return notificationService.createMessageDTO("Notification deleted");
        } else {
            return notificationService.createMessageDTO("Bad Request");
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long user_id) {
        List<NotificationDTO> rtn = notificationService.getUserNotifications(user_id);
            return ResponseEntity.ok(rtn);
    };
}
