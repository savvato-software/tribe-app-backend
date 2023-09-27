package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.annotations.controllers.NotificationController.DeleteNotification;
import com.savvato.tribeapp.controllers.annotations.controllers.NotificationController.GetNotificationById;
import com.savvato.tribeapp.controllers.annotations.controllers.NotificationController.UpdateNotification;
import com.savvato.tribeapp.dto.NotificationDTO;
import com.savvato.tribeapp.services.NotificationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/notifications")
@Tag(name="notifications", description="Notifications about status of user actions (e.g. approval of submitted attribute)")
public class NotificationAPIController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{id}")
    @GetNotificationById
    public ResponseEntity<NotificationDTO> getNotificationById(@Parameter(description="A notification ID", example = "1") @PathVariable Long id) {
        NotificationDTO notification = notificationService.getNotificationDTOById(id);
        return notification != null ? ResponseEntity.ok(notification) : ResponseEntity.notFound().build();
    }

    @UpdateNotification
    @PutMapping
    public ResponseEntity<String> updateNotification(@RequestBody Long id) {
        boolean isRead = notificationService.checkNotificationReadStatus(id);
        if (isRead) {
            return ResponseEntity.ok("Notification is already read");
        } else {
            notificationService.updateNotificationReadStatus(id);
            return ResponseEntity.ok("Notification read status updated");
        }
    }

    @DeleteNotification
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(@Parameter(description="A notification ID", example = "1") @PathVariable Long id) {
        boolean exists = notificationService.checkNotificationExists(id);
        if (exists) {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok("Notification deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
