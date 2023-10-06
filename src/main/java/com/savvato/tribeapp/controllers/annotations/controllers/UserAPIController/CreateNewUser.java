package com.savvato.tribeapp.controllers.annotations.controllers.UserAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.UserRequest;
import com.savvato.tribeapp.entities.User;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Create a new user",
    description = "Provided a valid UserRequest (see schema), create new user.")
@DocumentedRequestBody(description = "The user request", implementation = UserRequest.class)
@Success(description = "Successfully created new user", implementation = User.class)
@BadRequest(
    description = "Failed to create new user",
    example = "IllegalArgumentException: <error message description>")
public @interface CreateNewUser {}
