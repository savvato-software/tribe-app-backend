package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import com.savvato.tribeapp.services.ReviewDecisionReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review-decision-reason")
public class ReviewDecisionReasonAPIController {

    @Autowired
    ReviewDecisionReasonService reviewDecisionReasonService;

    @GetMapping
    public ResponseEntity<List<ReviewDecisionReasonDTO>> getReviewDecisionReasons() {

        List<ReviewDecisionReasonDTO> rdrDtoList = reviewDecisionReasonService.getReviewDecisionReasons();

        return ResponseEntity.status(HttpStatus.OK).body(rdrDtoList);

    }
}
