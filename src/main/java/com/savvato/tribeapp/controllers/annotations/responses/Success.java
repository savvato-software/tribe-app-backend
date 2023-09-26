package com.savvato.tribeapp.controllers.annotations.responses;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * In OpenAPI, include success response. Optionally pass specific response code, description,
 * content, and headers.
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Response()
@Inherited
@ApiResponse()
public @interface Success {
  String responseCode() default "200";

  String description() default "Successful operation";

  Content content() default @Content;

  Schema schema() default @Schema;

  String example() default "";

  Header[] headers() default {};

  ExampleObject[] examples() default {};

  Class<?> implementation() default Void.class;

  ArraySchema array() default @ArraySchema;
}
