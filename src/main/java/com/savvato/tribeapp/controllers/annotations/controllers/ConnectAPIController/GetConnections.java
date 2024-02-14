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
    summary = "Get connections for a user",
    description = "Provided a user ID, get Connections for a user. ")
@Success(description = "", example = "")
@BadRequest(description = "", noContent = true)
public @interface GetConnections{}
