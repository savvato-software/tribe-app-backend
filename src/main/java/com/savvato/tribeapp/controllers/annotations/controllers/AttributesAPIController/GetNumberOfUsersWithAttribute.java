package com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Get number of users with an attribute.",
        description = "Provided an attribute ID, get the number of users that share this attribute.")
@Success(
        description = "Successfully retrieved user attributes",
        implementation = Integer.class,
        example = "8")
@BadRequest(
        description = "Failed to retrieve number of users with this attribute",
        noContent = true)
public @interface GetNumberOfUsersWithAttribute {
}
