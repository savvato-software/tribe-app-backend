package com.savvato.tribeapp.controllers.annotations.controllers.UserAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Check if email address is available",
    description = "Provided an email address, check if it is available.")
@Success(
    description = "Status of attempt to check email availability",
    examples = {
      @ExampleObject(name = "Email address is available", value = "true"),
      @ExampleObject(name = "Email address is unavailable", value = "false")
    })
public @interface IsEmailAddressAvailable {}
