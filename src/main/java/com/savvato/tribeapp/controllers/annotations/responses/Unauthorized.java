package com.savvato.tribeapp.controllers.annotations.responses;

import java.lang.annotation.*;

/** In OpenAPI, include Unauthorized error as a possible response. */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Response(
    responseCode = "401",
    description = "User is not authorized for this action.",
    noContent = true)
public @interface Unauthorized {}
