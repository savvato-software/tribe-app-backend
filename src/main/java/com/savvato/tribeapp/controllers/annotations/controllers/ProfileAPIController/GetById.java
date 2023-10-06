package com.savvato.tribeapp.controllers.annotations.controllers.ProfileAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.dto.ProfileDTO;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

/** Documentation for getting a profile by its ID */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Get a profile by its ID",
    description = "Provided a valid ID, get user profile.")
@Success(description = "Successfully retrieved user profile", implementation = ProfileDTO.class)
@BadRequest(noContent = true, description = "Failed to retrieve user profile")
public @interface GetById {}
