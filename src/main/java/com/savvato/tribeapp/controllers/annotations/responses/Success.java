package com.savvato.tribeapp.controllers.annotations.responses;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * In OpenAPI, include success response. Optionally specify the following:
 *
 * <ul>
 *   <li>{@code responseCode}: The response code
 *   <li>{@code description}: The response description
 *   <li>{@code headers}: The response headers
 *   <li>{@code example}: An example of the response (use implementation argument if you wish to
 *       copy over the schema defined in a class, or schema argument if you wish to create your own
 *       schema)
 *   <li>{@code examples}: Multiple examples for this response
 *   <li>{@code implementation}: The implementation for this response (copies over the schema
 *       defined in the provided class)
 *   <li>{@code schema}: The schema for this response's content (use only if the above do not
 *       satisfy your modeling requirements)
 *   <li>{@code array}: An array, if your response contains it
 *   <li>{@code content}: The response's content (use only if the above do not satisfy your modeling
 *       requirements)
 *   <li>{@code noContent}: Defaults to false - the response has content
 * </ul>
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

  Header[] headers() default {};

  String example() default "";

  ExampleObject[] examples() default {};

  Class<?> implementation() default Void.class;

  Schema schema() default @Schema;

  ArraySchema array() default @ArraySchema;

  Content content() default @Content;

  boolean noContent() default false;
}
