package com.savvato.tribeapp.controllers.annotations.controllers.ToBeReviewedAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.BadRequest;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.entities.ToBeReviewed;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Get the next phrase to review",
    description = "Get the next phrase to review.")
@Success(description = "Found next phrase", implementation = ToBeReviewed.class)
@BadRequest(
    noContent = true,
    description = "There is no phrase to be reviewed next, or it could not be retrieved.")
public @interface GetPhrase {}
