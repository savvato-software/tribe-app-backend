package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.dto.ReviewDecisionRequest;
import com.savvato.tribeapp.dto.ReviewDecisionDTO;
import com.savvato.tribeapp.entities.ReviewDecision;
import com.savvato.tribeapp.services.ReviewDecisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ReviewDecisionAPIController {
    @Autowired
    ReviewDecisionService reviewDecisionService;

    @RequestMapping(value = { "/api/reviewer-decision" }, method= RequestMethod.POST)
    public ResponseEntity<ReviewDecisionDTO> saveReviewDecision(@RequestBody @Valid ReviewDecisionRequest request) {
        ReviewDecision decisionSaved = reviewDecisionService.saveReviewDecision(request.reviewId, request.reviewerId, request.reasonId);
        ReviewDecisionDTO rtn = ReviewDecisionDTO.builder().build();

        rtn.reviewId = decisionSaved.getReviewId();
        rtn.userId = decisionSaved.getUserId();
        rtn.reasonId = decisionSaved.getReasonId();
        return ResponseEntity.status(HttpStatus.OK).body(rtn);
    }
}
