package com.savvato.tribeapp.controllers.annotations.controllers.AttributesAPIController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.*;

/** Documentation for deleting a phrase association for a user */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Delete association of this phrase from this user.",
    description = "Provide a phrase ID, delete UserPhrase record from user_phrase table.")
@Success(
    description = "Successfully deleted UserPhrase.")
public @interface DeletePhraseFromUser {}
