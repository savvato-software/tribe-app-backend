package com.savvato.tribeapp.controllers.annotations.controllers.FileUploadController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import java.lang.annotation.*;
import java.util.Date;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Check if file has been found",
    description =
        "Provided a resource type and a resource ID, return timestamp of file if it exists.")
@Success(description = "Found file", implementation = Date.class)
public @interface FileIsFound {}
