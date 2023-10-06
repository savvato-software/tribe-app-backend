package com.savvato.tribeapp.controllers.annotations.controllers.PermissionsAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.PermissionsRequest;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

/** Documentation for deleting user permission(s) */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Un-assign roles from a user",
    description = "Provided a valid PermissionsRequest (see schema), remove a role from the user. ")
@DocumentedRequestBody(implementation = PermissionsRequest.class)
@Success(description = "Success deleting roles", example = "true", implementation = Boolean.class)
@BadRequest(
    description = "Failed to delete roles",
    implementation = Boolean.class,
    example = "false")
public @interface DeletePermissions {}
