package com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

/** Documentation for getting all cosigns for a user */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Get all cosigners of attributes for a given attribute for a user",
        description = "Provided a user ID, get cosigns and attribute ids for that user. ")
@Success(description = "", example = "")
@BadRequest(description = "", noContent = true)
public @interface GetAllCosignsForUser {}
