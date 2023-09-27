package com.savvato.tribeapp.controllers.annotations.requests;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.lang.annotation.*;

/** In OpenAPI, specify the request body. Optionally pass specific description. */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestBody()
public @interface DocumentedRequestBody {
  String description() default "The request body for this operation.";

  Content content() default @Content;

  String example() default "";

  ArraySchema array() default @ArraySchema;

  Schema schema() default @Schema;

  Class<?> implementation() default Void.class;
}
