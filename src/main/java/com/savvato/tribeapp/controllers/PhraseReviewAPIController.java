package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.PermissionsRequest;
import com.savvato.tribeapp.entities.Phrase;
import com.savvato.tribeapp.repositories.PhraseRepository;
import com.savvato.tribeapp.services.PhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class PhraseReviewAPIController {

    @Autowired
    PhraseRepository pr;

    @Autowired
    PhraseService phraseService;

    @RequestMapping(value = { "/api/review" }, method= RequestMethod.GET)
    public ResponseEntity getPhrase() {
        Optional<Phrase> opt = phraseService.getReviewPhrase();

        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
