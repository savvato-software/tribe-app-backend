package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.AttributesRequest;
import com.savvato.tribeapp.dto.AttributeDTO;
import com.savvato.tribeapp.services.AttributesService;
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
