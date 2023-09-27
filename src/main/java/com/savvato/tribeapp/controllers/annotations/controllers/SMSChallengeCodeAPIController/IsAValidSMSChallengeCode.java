package com.savvato.tribeapp.controllers.annotations.controllers.SMSChallengeCodeAPIController;

import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import com.savvato.tribeapp.controllers.annotations.responses.Success;
import com.savvato.tribeapp.controllers.dto.SMSChallengeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.lang.annotation.*;

/** Documentation for sending SMS challenge code to user */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Check validity of SMS challenge code",
    description = "Provided a valid SMSChallengeRequest (see schema), get a challenge code.")
@DocumentedRequestBody(
    description = "A request containing an SMS challenge code",
    implementation = SMSChallengeRequest.class)
@Success(
    description = "Validity of challenge code",
    implementation = Boolean.class,
    examples = {
      @ExampleObject(name = "Valid challenge code", value = "true"),
      @ExampleObject(name = "Invalid challenge code", value = "false"),
    })
public @interface IsAValidSMSChallengeCode {}
