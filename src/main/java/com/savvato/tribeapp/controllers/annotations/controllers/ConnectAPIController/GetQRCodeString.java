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
    summary = "Get a user's QR code string",
    description = "Provided a user ID, get the user's QR Code. ")
@Success(description = "Successfully retrieved QR code", example = "ABCDEFGHIJKL")
@BadRequest(description = "No QR code found", noContent = true)
public @interface GetQRCodeString {}
