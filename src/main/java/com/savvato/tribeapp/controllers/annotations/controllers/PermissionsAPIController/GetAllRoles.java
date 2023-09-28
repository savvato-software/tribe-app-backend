package com.savvato.tribeapp.controllers.annotations.controllers.PermissionsAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadGateway;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.*;

/** Documentation for getting a list of all user roles. */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(summary = "Get all user roles", description = "Get all user roles")
@Success(
    description = "Found all user roles",
    array =
        @ArraySchema(
            schema =
                @Schema(enumAsRef = true, ref = "#/components/schemas/UserRole/properties/ROLES")))
@BadGateway
public @interface GetAllRoles {}
