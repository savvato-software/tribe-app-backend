package com.savvato.tribeapp.controllers.annotations.responses;

import java.lang.annotation.*;

/**
 * In OpenAPI, include Not Found response as a possible response. Optionally specify the following:
 *
 * <ul>
 *   <li>{@code responseCode}: The response code
 *   <li>{@code description}: The response description
 *   <li>{@code noContent}: Defaults to true - the response has no content
 * </ul>
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Response()
public @interface NotFound {
  String responseCode() default "404";

  String description() default "Not Found";

  boolean noContent() default true;
}
