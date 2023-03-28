package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.AttributesRequest;
import com.savvato.tribeapp.dto.AttributesDTO;
import com.savvato.tribeapp.services.AttributesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class AttributesAPIController {

    @Autowired
    AttributesService attributesService;

    AttributesAPIController() {

    }

    // if the phrase has been reviewed, then apply to the user
    // if not then save it to the table itself
    // controller defines the endpoint
    // in other words, this is the endpoint
    @RequestMapping(value = { "/api/attributes" }, method=RequestMethod.POST)
    public ResponseEntity<Boolean> applyPhraseToUser(@RequestBody @Valid AttributesRequest req) {
        boolean rtn = false;

        if( req.noun != null && req.verb != null )
        {
            rtn = attributesService.isFourWordsValid(req.noun, req.verb, req.preposition, req.adverb);
        }

        if (rtn)
            return ResponseEntity.status(HttpStatus.OK).body(true);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

}