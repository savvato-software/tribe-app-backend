package com.savvato.tribeapp.controllers.annotations.responses;

import java.lang.annotation.*;

/** In OpenAPI, include Bad Gateway error with null body as a possible response. */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Response(
    responseCode = "500",
    description = "The operation failed with a bad gateway error.",
    noContent = true)
public @interface BadGateway {}
