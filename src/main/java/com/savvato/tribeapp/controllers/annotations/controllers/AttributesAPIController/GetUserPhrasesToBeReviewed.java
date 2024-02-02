package com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.dto.ToBeReviewedDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.*;

/** Documentation for getting a user's to be reviewed phrases */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Get phrases to be reviewed for a user",
        description = "Provided a valid user ID, get phrases to be reviewed for that user.")
@Success(
        description = "Successfully retrieved user to be reviewed phrases",
        array = @ArraySchema(schema = @Schema(implementation = ToBeReviewedDTO.class)))
@BadRequest(description = "Failed to retrieve to be reviewed phrases for user.", noContent = true)
public @interface GetUserPhrasesToBeReviewed {}
