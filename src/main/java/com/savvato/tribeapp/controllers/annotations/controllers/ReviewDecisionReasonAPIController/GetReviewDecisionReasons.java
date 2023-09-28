package com.savvato.tribeapp.controllers.annotations.controllers.ReviewDecisionReasonAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.dto.ReviewDecisionReasonDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Get all review decision reasons",
    description = "Get the list of all review decision reasons")
@Success(
    description = "Successfully retrieved all review decision reasons",
    array = @ArraySchema(schema = @Schema(implementation = ReviewDecisionReasonDTO.class)))
public @interface GetReviewDecisionReasons {}
