package com.savvato.tribeapp.controllers.annotations.controllers.UserAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Check if user information is unique",
    description = "Provided a username, phone, and email, check if all items are unique.")
@Success(
    description = "Status of attempt to check user information uniqueness",
    examples = {
      @ExampleObject(name = "All details are unique", value = "true"),
      @ExampleObject(name = "Username is unavailable", value = "{\"response\": \"username\"}"),
      @ExampleObject(name = "Phone number is unavailable", value = "{\"response\": \"phone\"}"),
      @ExampleObject(name = "Email address is unavailable", value = "{\"response\": \"email\"}")
    })
public @interface IsUserInformationUnique {}
