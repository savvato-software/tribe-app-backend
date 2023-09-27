package com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.ConnectRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.lang.annotation.*;

/** Documentation for connecting */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Establish connection between two users",
    description = "Provided a ConnectRequest (see schemas), save the connection.")
@DocumentedRequestBody(description = "A request to connect", implementation = ConnectRequest.class)
@Success(
    description = "Status of attempt to establish connection",
    examples = {
      @ExampleObject(name = "Connection established successfully", value = "true"),
      @ExampleObject(name = "Failed to establish connection", value = "false")
    })
public @interface Connect {}
