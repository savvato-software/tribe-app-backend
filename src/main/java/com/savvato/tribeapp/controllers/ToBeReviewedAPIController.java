package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.annotations.controllers.ToBeReviewedAPIController.GetPhrase;
import com.savvato.tribeapp.entities.ToBeReviewed;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import com.savvato.tribeapp.services.ToBeReviewedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
@Tag(name = "review", description = "Attributes that are yet to be reviewed")
public class ToBeReviewedAPIController {

  @Autowired ToBeReviewedRepository pr;

  @Autowired ToBeReviewedService toBeReviewedService;

  @GetPhrase
  @GetMapping
  public ResponseEntity getPhrase() {
    Optional<ToBeReviewed> opt = toBeReviewedService.getReviewPhrase();

    if (opt.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(opt.get());
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
  }
}
