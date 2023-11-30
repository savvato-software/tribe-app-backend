package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController.ApplyPhraseToUser;
import com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController.DeletePhraseFromUser;
import com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController.GetAttributesForUser;
import com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController.GetUserPhrasesToBeReviewed;
import com.savvato.tribeapp.controllers.dto.AttributesRequest;
import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.dto.ToBeReviewedDTO;
import com.savvato.tribeapp.entities.NotificationType;
import com.savvato.tribeapp.services.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attributes")
@Tag(
    name = "attributes",
    description = "Everything about attributes, e.g. \"plays chess competitively\"")
public class AttributesAPIController {

  @Autowired 
  AttributesService attributesService;

  @Autowired
  PhraseService phraseService;

  @Autowired 
  NotificationService notificationService;

  @Autowired
  UserPhraseService userPhraseService;

  @Autowired
  ReviewSubmittingUserService reviewSubmittingUserService;

  AttributesAPIController() {}

  @GetAttributesForUser
  @GetMapping("/{userId}")
  public ResponseEntity<List<AttributeDTO>> getAttributesForUser(
      @Parameter(description = "User ID of user", example = "1") @PathVariable Long userId) {

    Optional<List<AttributeDTO>> opt = attributesService.getAttributesByUserId(userId);

    if (opt.isPresent()) return ResponseEntity.status(HttpStatus.OK).body(opt.get());
    else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  @GetUserPhrasesToBeReviewed
  @GetMapping("/in-review/{userId}")
  public ResponseEntity<List<ToBeReviewedDTO>> getUserPhrasesToBeReviewed(
          @Parameter(description = "User ID of user", example = "1")
          @PathVariable Long userId) {
    List<ToBeReviewedDTO> rtn = reviewSubmittingUserService.getUserPhrasesToBeReviewed(userId);
      return ResponseEntity.status(HttpStatus.OK).body(rtn);
    }

  @ApplyPhraseToUser
  @PostMapping
  public ResponseEntity<Boolean> applyPhraseToUser(@RequestBody @Valid AttributesRequest req) {
    if (phraseService.isPhraseValid(req.adverb, req.verb, req.preposition, req.noun)) {
      boolean isPhraseApplied =
          phraseService.applyPhraseToUser(
              req.userId, req.adverb, req.verb, req.preposition, req.noun);
      if (isPhraseApplied) {
        sendNotification(true, req.userId);
        return ResponseEntity.status(HttpStatus.OK).body(true);
      } else {
        sendNotification(false, req.userId);
        return ResponseEntity.status(HttpStatus.OK).body(false);
      }
    } else {
      sendNotification(false, req.userId);
      return ResponseEntity.status(HttpStatus.OK).body(false);
    }
  }

  ///api/attributes/?phraseId=xx&userId=xx
  @DeletePhraseFromUser
  @DeleteMapping
  public ResponseEntity deletePhraseFromUser(@Parameter(description = "Phrase ID of phrase", example = "1") @RequestParam("phraseId") Long phraseId, @Parameter(description = "User ID of user", example = "1") @RequestParam("userId") Long userId) {
      userPhraseService.deletePhraseFromUser(phraseId, userId);
      return ResponseEntity.ok().build();
  }

  private void sendNotification(Boolean approved, Long userId) {
    if (approved) {
      notificationService.createNotification(
          NotificationType.ATTRIBUTE_REQUEST_APPROVED,
          userId,
          NotificationType.ATTRIBUTE_REQUEST_APPROVED.getName(),
          "Your attribute has been approved!");

    } else {
      notificationService.createNotification(
          NotificationType.ATTRIBUTE_REQUEST_REJECTED,
          userId,
          NotificationType.ATTRIBUTE_REQUEST_REJECTED.getName(),
          "Your attribute was rejected. This attribute is unsuitable and cannot be applied to users.");
    }
  }
}
