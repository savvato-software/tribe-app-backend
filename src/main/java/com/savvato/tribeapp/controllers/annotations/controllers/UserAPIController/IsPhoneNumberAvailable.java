package com.savvato.tribeapp.controllers.annotations.controllers.UserAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Check if phone number is available",
    description = "Provided a phone number, check if it is available.")
@Success(
    description = "Status of attempt to check phone number availability",
    examples = {
      @ExampleObject(name = "Phone number is available", value = "true"),
      @ExampleObject(name = "Phone number is unavailable", value = "false")
    })
public @interface IsPhoneNumberAvailable {}
