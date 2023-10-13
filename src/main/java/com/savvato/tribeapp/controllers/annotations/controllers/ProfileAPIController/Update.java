package com.savvato.tribeapp.controllers.annotations.controllers.ProfileAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.ProfileRequest;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

/** Documentation for updating a profile by its ID */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Update a profile by its ID",
    description = "Provided a valid ProfileRequest (see schema), update user profile.")
@DocumentedRequestBody(implementation = ProfileRequest.class)
@Success(
    description = "Success updating profile.",
    implementation = Boolean.class,
    example = "true")
@BadRequest(
    description = "Failed to update profile",
    implementation = Boolean.class,
    example = "false")
public @interface Update {}
