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
public class AttributesAPIController {

    @Autowired
    AttributesService attributesService;

    @Autowired
    PhraseService phraseService;

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

        if (req.noun == null || req.noun.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (req.verb == null || req.verb.trim().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        if (phraseService.isEveryWordValid(req.adverb, req.verb, req.preposition, req.noun)) {
            phraseService.applyPhraseToUser(req.userId, req.adverb, req.verb, req.preposition, req.noun);
            rtn = true; // added

        }

        if (rtn)
            return ResponseEntity.status(HttpStatus.OK).body(true);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
}
