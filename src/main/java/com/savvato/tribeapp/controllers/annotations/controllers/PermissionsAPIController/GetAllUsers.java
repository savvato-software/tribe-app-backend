package com.savvato.tribeapp.controllers.annotations.controllers.PermissionsAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadGateway;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.*;

/** Documentation for getting a list of all users. */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Get all users", description = "Get all users")
@Success(array = @ArraySchema(minItems = 2, schema = @Schema(implementation = User.class)))
@BadGateway
public @interface GetAllUsers {}
