package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.AttributesRequest;
import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.entities.Notification;
import com.savvato.tribeapp.entities.NotificationType;
import com.savvato.tribeapp.services.AttributesService;
import com.savvato.tribeapp.services.NotificationService;
import com.savvato.tribeapp.services.PhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attributes")
public class AttributesAPIController {

    @Autowired
    AttributesService attributesService;

    @Autowired
    PhraseService phraseService;

    @Autowired
    NotificationService notificationService;

    AttributesAPIController() {

    }

    @GetMapping( "/{userId}")
    public ResponseEntity<List<AttributeDTO>> getAttributesForUser(@PathVariable Long userId) {

        Optional<List<AttributeDTO>> opt = attributesService.getAttributesByUserId(userId);

        if (opt.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PostMapping
    public ResponseEntity<Boolean> applyPhraseToUser(@RequestBody @Valid AttributesRequest req) {
        if (phraseService.isPhraseValid(req.adverb, req.verb, req.preposition, req.noun)) {
            phraseService.applyPhraseToUser(req.userId, req.adverb, req.verb, req.preposition, req.noun);
            sendNotification(true, req.userId);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }
        sendNotification(false, req.userId);
        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    private void sendNotification(Boolean approved, Long userId) {
        if (approved) {
            notificationService.createNotification(
                    NotificationType.ATTRIBUTE_REQUEST_REJECTED,
                    userId,
                    NotificationType.ATTRIBUTE_REQUEST_REJECTED.getName(),
                    "Your attribute was rejected. This attribute is unsuitable and cannot be applied to users.");
        } else {
            notificationService.createNotification(
                    NotificationType.ATTRIBUTE_REQUEST_APPROVED,
                    userId,
                    NotificationType.ATTRIBUTE_REQUEST_APPROVED.getName(),
                    "Your attribute has been approved!");
        }
    }

}
