package com.savvato.tribeapp.controllers.annotations.controllers.FileUploadController;

import com.savvato.tribeapp.controllers.annotations.responses.Success;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
    summary = "Handle a file upload",
    description = "Provided a resource type, a resource ID, and a file, upload it.")
@Success(
    description = "Status of attempt to upload file",
    examples = {
      @ExampleObject(name = "Uploaded successfully", value = "{\"msg\":\"error\"}"),
      @ExampleObject(name = "Failed to upload file", value = "{\"msg\":\"ok\"}"),
      @ExampleObject(name = "Invalid resource type", value = "")
    })
public @interface HandleFileUpload {}
