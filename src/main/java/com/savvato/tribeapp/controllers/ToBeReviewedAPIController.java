package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.dto.ToBeReviewedDTO;
import com.savvato.tribeapp.repositories.ToBeReviewedRepository;
import com.savvato.tribeapp.services.ToBeReviewedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/review")
public class ToBeReviewedAPIController {

    @Autowired
    ToBeReviewedRepository pr;

    @Autowired
    ToBeReviewedService toBeReviewedService;

    @GetMapping
    public ResponseEntity getPhrase() {
        Optional<ToBeReviewedDTO> opt = toBeReviewedService.getReviewPhrase();

        if (opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(opt.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
