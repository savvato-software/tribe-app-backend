package com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

/** Documentation for saving a cosign from one user to another*/
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Delete a cosigned phrase",
    description = "Provide an issuing user ID, receiving user ID and phrase ID, save as record in cosign table.")
@Success(
    description = "Successfully deleted cosign.")
public @interface DeleteCosign {}
