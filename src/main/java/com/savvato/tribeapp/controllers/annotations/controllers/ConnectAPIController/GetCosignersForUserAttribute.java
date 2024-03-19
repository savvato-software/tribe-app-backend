package com.savvato.tribeapp.controllers.annotations.controllers.ConnectAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

/** Documentation for getting cosigns for a user attribute */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Get all cosigners for a given attribute for a user",
        description = "Provided a user ID and attribute ID, get cosigns for that user and attribute. ")
@Success(description = "", example = "")
@BadRequest(description = "", noContent = true)
public @interface GetCosignersForUserAttribute {}
