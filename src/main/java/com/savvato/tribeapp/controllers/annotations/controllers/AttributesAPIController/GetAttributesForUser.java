package com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.dto.PhraseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.*;

/** Documentation for getting attributes for a user */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Get attributes for a user",
    description = "Provided a valid user ID, get user attributes. ")
@Success(
    description = "Successfully retrieved user attributes",
    array = @ArraySchema(schema = @Schema(implementation = PhraseDTO.class), minItems = 2))
@BadRequest(
    description = "Failed to retrieve attributes for user.",
    example = "false",
    implementation = Boolean.class)
public @interface GetAttributesForUser {}
