package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.controllers.annotations.controllers.ReviewDecisionReasonAPIController.GetReviewDecisionReasons;
import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import com.savvato.tribeapp.services.ReviewDecisionReasonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "review-decision-reason", description = "Review decision reasons")
@RequestMapping("/api/review-decision-reason")
public class ReviewDecisionReasonAPIController {

  @Autowired ReviewDecisionReasonService reviewDecisionReasonService;

  @GetReviewDecisionReasons
  @GetMapping
  public ResponseEntity<List<ReviewDecisionReasonDTO>> getReviewDecisionReasons() {

    List<ReviewDecisionReasonDTO> rdrDtoList =
        reviewDecisionReasonService.getReviewDecisionReasons();

    return ResponseEntity.status(HttpStatus.OK).body(rdrDtoList);
  }
}
