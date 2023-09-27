package com.savvato.tribeapp.controllers.annotations.controllers.FileUploadController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Serve a file",
    description = "Provided a resource type, a resource ID, and a photo size, serve a file.")
@Success(
    description = "Successfully loaded file",
    array = @ArraySchema(schema = @Schema(implementation = Byte.class)))
public @interface ServeFile {}
