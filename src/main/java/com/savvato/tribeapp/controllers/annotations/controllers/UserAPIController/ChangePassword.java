package com.savvato.tribeapp.controllers.annotations.controllers.UserAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.BadGateway;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.ChangePasswordRequest;
import com.savvato.tribeapp.entities.User;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Change password",
    description = "Provided a valid ChangePasswordRequest (see schema), change password.")
@DocumentedRequestBody(
    description = "The password change request",
    implementation = ChangePasswordRequest.class)
@Success(description = "Successfully changed password", implementation = User.class)
@BadGateway
public @interface ChangePassword {}
