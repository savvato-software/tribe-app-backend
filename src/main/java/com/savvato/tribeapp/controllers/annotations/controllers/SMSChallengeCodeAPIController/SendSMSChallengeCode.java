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
    summary = "Send an SMS Challenge Code",
    description = "Provided a valid SMSChallengeRequest (see schema), get a challenge code. ")
@DocumentedRequestBody(
    description = "A request for an SMS challenge code",
    implementation = SMSChallengeRequest.class)
@Success(
    description = "Status of attempt to send challenge code",
    examples = {
      @ExampleObject(
          name = "Success",
          description = "Success sending challenge code",
          value = "1234567890"),
      @ExampleObject(
          name = "Error",
          description = "Failure sending challenge code",
          value = "error sending sms challenge to [phone number]"),
    })
public @interface SendSMSChallengeCode {}
