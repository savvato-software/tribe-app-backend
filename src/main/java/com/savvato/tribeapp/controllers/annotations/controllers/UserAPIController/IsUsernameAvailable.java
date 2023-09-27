package com.savvato.tribeapp.controllers.annotations.controllers.UserAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Check if username is available",
    description = "Provided a username, check if it is available.")
@Success(
    description = "Status of attempt to check username availability",
    examples = {
      @ExampleObject(name = "Username is available", value = "true"),
      @ExampleObject(name = "Username is unavailable", value = "false")
    })
public @interface IsUsernameAvailable {}
