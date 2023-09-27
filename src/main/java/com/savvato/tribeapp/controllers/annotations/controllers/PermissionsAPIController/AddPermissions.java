package com.savvato.tribeapp.controllers.annotations.controllers.PermissionsAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.PermissionsRequest;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

/** Documentation for adding permissions */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Assign new roles to a user",
    description = "Provided a valid PermissionsRequest (see schema), assign a role to the user.")
@DocumentedRequestBody(implementation = PermissionsRequest.class)
@Success(description = "Success assigning roles", implementation = Boolean.class, example = "true")
@BadRequest(
    description = "Failed to assign roles",
    implementation = Boolean.class,
    example = "false")
public @interface AddPermissions {}
