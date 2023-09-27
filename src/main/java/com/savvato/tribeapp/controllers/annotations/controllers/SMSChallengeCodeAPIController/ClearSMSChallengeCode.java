package com.savvato.tribeapp.controllers.annotations.controllers.SMSChallengeCodeAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;

/** Documentation for sending SMS challenge code to user */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Clear the SMS Challenge Code",
    description = "Clear the SMS challenge code. Return 'ok' no matter what.")
@Success(description = "Status of attempt to clear challenge code", example = "ok")
public @interface ClearSMSChallengeCode {}
