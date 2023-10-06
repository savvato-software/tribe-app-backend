package com.savvato.tribeapp.controllers.annotations.controllers.ReviewDecisionAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.ReviewDecisionRequest;
import com.savvato.tribeapp.dto.ReviewDecisionDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Save review decision",
    description = "Given a valid ReviewDecisionRequest (see schema), save the review decision.")
@DocumentedRequestBody(implementation = ReviewDecisionRequest.class)
@Success(
    description = "Successfully saved review decision",
    implementation = ReviewDecisionDTO.class)
public @interface SaveReviewDecision {}
