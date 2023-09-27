package com.savvato.tribeapp.controllers.annotations.controllers.ReviewDecisionAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.dto.ReviewDecisionDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Save review decision",
    description = "Given a valid ReviewDecisionRequest (see schema), save the review decision.")
@Success(
    description = "Successfully saved review decision",
    implementation = ReviewDecisionDTO.class)
@BadRequest(
    noContent = true,
    description = "There is no phrase to be reviewed next, or it could not be retrieved.")
public @interface SaveReviewDecision {}
