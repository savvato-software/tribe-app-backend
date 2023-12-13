package com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

/** Documentation for getting a QR code string */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Get all the connections for a given user",
    description = "Get all the connections for a given user")
@Success(description = "A List of all Connections for a given user")
@BadRequest(description = "400 null", noContent = true)
public @interface GetConnections {}
