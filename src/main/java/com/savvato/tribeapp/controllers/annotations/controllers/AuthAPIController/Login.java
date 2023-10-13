package com.savvato.tribeapp.controllers.annotations.controllers.AuthAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.annotations.responses.Unauthorized;
import com.savvato.tribeapp.controllers.dto.AuthRequest;
import com.savvato.tribeapp.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.*;

/** Documentation for logging in */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DocumentedRequestBody(implementation = AuthRequest.class)
@Operation(summary = "Log in", description = "Provided an AuthRequest (see schemas), log user in.")
@Success(
    description = "Successful login attempt",
    headers =
        @Header(
            name = "Authorization",
            required = true,
            schema = @Schema(example = "ABCDEFGHIJKL")),
    implementation = User.class)
@Unauthorized
public @interface Login {}
