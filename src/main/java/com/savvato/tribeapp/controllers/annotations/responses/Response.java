package com.savvato.tribeapp.controllers.annotations.responses;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.*;

import java.lang.annotation.*;

/**
 * <strong>Warning: </strong> This class is the 'base' response annotation class. Unless you are
 * specifying a response with a new response code, you would likely want to use a more specific
 * response annotation class (such as {@code @Success}/{@code @NotFound}) instead. Those are more
 * abstracted and easier to use.
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Response {

  String mediaType() default "";

  String responseCode() default "200";

  String description() default "Successful operation";

  ExampleObject[] examples() default {};

  Header[] headers() default {};

  String example() default "";

  Class<?> implementation() default Void.class;

  Content content() default @Content;

  Schema schema() default @Schema;

  ArraySchema array() default @ArraySchema;

  boolean noContent() default false;
}
