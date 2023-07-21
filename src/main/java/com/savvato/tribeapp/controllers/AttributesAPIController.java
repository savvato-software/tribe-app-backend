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
public class AttributesAPIController {

    @Autowired
    AttributesService attributesService;

    @Autowired
    PhraseService phraseService;

    @Autowired
    NotificationService notificationService;

    AttributesAPIController() {

    }

    @RequestMapping(value = { "/api/attributes/{userId}" }, method=RequestMethod.GET)
    public ResponseEntity<List<AttributeDTO>> getAttributesForUser(@PathVariable Long userId) {

        Optional<List<AttributeDTO>> opt = attributesService.getAttributesByUserId(userId);

        if (opt.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @RequestMapping(value = { "/api/attributes" }, method=RequestMethod.POST)
    public ResponseEntity<Boolean> applyPhraseToUser(@RequestBody @Valid AttributesRequest req) {
        boolean rtn = false;

        if (phraseService.isPhraseValid(req.adverb, req.verb, req.preposition, req.noun)) {
            phraseService.applyPhraseToUser(req.userId, req.adverb, req.verb, req.preposition, req.noun);
            rtn = true;
        }

        if (rtn) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

        Notification notification = notificationService.createNotification(
                    NotificationType.ATTRIBUTE_REQUEST_REJECTED,
                    req.userId,
                    NotificationType.ATTRIBUTE_REQUEST_REJECTED.getName(),
                    "Your attribute was rejected. This attribute is unsuitable and cannot be applied to users.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
