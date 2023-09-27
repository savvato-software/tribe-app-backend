package com.savvato.tribeapp.controllers.annotations.controllers.FileUploadController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Delete a file",
    description = "Provided a resource type and a resource ID, delete a file.")
@Success(
    description = "Status of attempt to delete file",
    examples = {
      @ExampleObject(name = "Deleted successfully", value = "true"),
      @ExampleObject(name = "Failed to delete file", value = "false")
    })
public @interface DeleteFile {}
