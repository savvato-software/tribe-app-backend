package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController.ApplyPhraseToUser;
import com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController.GetAttributesForUser;
import com.savvato.tribeapp.controllers.dto.AttributesRequest;
import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.services.AttributesService;
import com.savvato.tribeapp.services.PhraseService;
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

  @Autowired AttributesService attributesService;

  @Autowired PhraseService phraseService;

  AttributesAPIController() {}

  @GetAttributesForUser
  @GetMapping("/{userId}")
  public ResponseEntity<List<AttributeDTO>> getAttributesForUser(
      @Parameter(description = "User ID of user", example = "1") @PathVariable Long userId) {

    Optional<List<AttributeDTO>> opt = attributesService.getAttributesByUserId(userId);

    if (opt.isPresent()) return ResponseEntity.status(HttpStatus.OK).body(opt.get());
    else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
  }

  @ApplyPhraseToUser
  @PostMapping
  public ResponseEntity<Boolean> applyPhraseToUser(@RequestBody @Valid AttributesRequest req) {
    ResponseEntity rtn;

    if (phraseService.isPhraseValid(req.adverb, req.verb, req.preposition, req.noun)) {
      phraseService.applyPhraseToUser(req.userId, req.adverb, req.verb, req.preposition, req.noun);
      rtn = ResponseEntity.status(HttpStatus.OK).body(true);
    } else {
      rtn = ResponseEntity.status(HttpStatus.OK).body(false);
    }

    return rtn;
  }
}
