package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.NotificationRequest;
import com.savvato.tribeapp.controllers.annotations.controllers.NotificationController.DeleteNotification;
import com.savvato.tribeapp.controllers.annotations.controllers.NotificationController.GetNotificationById;
import com.savvato.tribeapp.controllers.annotations.controllers.NotificationController.UpdateNotification;
import com.savvato.tribeapp.dto.NotificationDTO;
import com.savvato.tribeapp.entities.Notification;
import com.savvato.tribeapp.services.NotificationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(
    name = "notifications",
    description =
        "Notifications about status of user actions (e.g. approval of submitted attribute)")
public class NotificationAPIController {

  @Autowired private NotificationService notificationService;

  @UpdateNotification
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

  @DeleteNotification
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteNotification(
      @Parameter(description = "A notification ID", example = "1") @PathVariable Long id) {
    boolean exists = notificationService.checkNotificationExists(id);
    if (exists) {
      notificationService.deleteNotification(id);
      return ResponseEntity.ok("Notification deleted");
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/user/{user_id}")
  public ResponseEntity<List<NotificationDTO>> getUserNotifications(@PathVariable Long user_id) {
    List<NotificationDTO> rtn = notificationService.getUserNotifications(user_id);
    return ResponseEntity.ok(rtn);
  }
  ;
}
