package com.savvato.tribeapp.controllers.annotations.config.utils;

import com.savvato.tribeapp.controllers.annotations.config.builders.SchemaBuilder;
import com.savvato.tribeapp.controllers.annotations.responses.Response;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.MediaType;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.fn.builders.schema.Builder;

public class ResponseAnnotationUtils extends AnnotationsUtils {
  private static final Builder SchemaAnnotationBuilder = Builder.schemaBuilder();

  /**
   * Gets OpenAPI Content model from @Response annotation
   *
   * @param responseAnnotation The @Response annotation
   * @return the corresponding OpenAPI content model
   */
  public static Optional<io.swagger.v3.oas.models.media.Content> getContent(
      Response responseAnnotation) {

    if (responseAnnotation.noContent() || responseAnnotation.schema().hidden())
      return Optional.empty();
    MediaType mediaType = getMediaType(responseAnnotation);
    io.swagger.v3.oas.models.media.Content content = new io.swagger.v3.oas.models.media.Content();
    content.addMediaType(
        containsPrimitiveSchema(responseAnnotation) ? "*/*" : "application/json", mediaType);
    return Optional.of(content);
  }

  /**
   * Gets description from @Response annotation
   *
   * @param responseAnnotation The @Response annotation
   * @return the description wrapped in an Optional
   */
  public static Optional<String> getDescription(Response responseAnnotation) {
    if (StringUtils.isNotBlank(responseAnnotation.description())) {
      return Optional.of(responseAnnotation.description());
    }
    return Optional.empty();
  }

  /**
   * Gets OpenAPI MediaType model from @Response annotation
   *
   * @param responseAnnotation - The @Response annotation
   * @return the corresponding OpenAPI MediaType model
   */
  private static MediaType getMediaType(Response responseAnnotation) {
    MediaType mediaType = new MediaType();
    getSchema(responseAnnotation).ifPresent(mediaType::schema);
    getExamples(mediaType, responseAnnotation.examples());
    return mediaType;
  }

  /**
   * Gets OpenAPI schema model from a response annotation. Includes array (if present) and custom
   * schema (if present) in the model returned.
   *
   * @param responseAnnotation The @Response annotation
   * @return the corresponding OpenAPI schema model
   */
  private static Optional<io.swagger.v3.oas.models.media.Schema<?>> getSchema(
      Response responseAnnotation) {
    SchemaBuilder schemaBuilder = SchemaBuilder.builder();
    getSchemaFromResponseAnnotation(responseAnnotation).ifPresent(schemaBuilder::merge);
    getSchemaFromSchemaAnnotation(responseAnnotation.schema()).ifPresent(schemaBuilder::merge);
    getArraySchemaFromAnnotation(responseAnnotation.array()).ifPresent(schemaBuilder::merge);
    return (schemaBuilder.build() != null) ? Optional.of(schemaBuilder.build()) : Optional.empty();
  }

  /**
   * Gets schema model from @Response annotation, <strong>assuming no custom schema or array is
   * present.</strong>
   *
   * @param responseAnnotation The @Response annotation
   * @return The corresponding OpenAPI schema model
   */
  private static Optional<io.swagger.v3.oas.models.media.Schema<?>> getSchemaFromResponseAnnotation(
      Response responseAnnotation) {
    /*
     Creating two schemas and merging them is necessary because the schema produced by resolving
     implementation() does not contain any specified non-implementation properties, such as title.
    */
    Schema nonImplementationSchemaAnnotation =
        SchemaAnnotationBuilder.example(responseAnnotation.example())
            .hidden(responseAnnotation.noContent())
            .build();
    // TODO: see if this can be improved - currently it fires even if no non-implementation
    // properties exist
    SchemaBuilder schemaBuilder = SchemaBuilder.builder();
    resolveSchemaFromType(responseAnnotation.implementation()).ifPresent(schemaBuilder::merge);
    getSchemaFromAnnotation(nonImplementationSchemaAnnotation, null)
        .ifPresent(schemaBuilder::merge);

    return (schemaBuilder.build() != null) ? Optional.of(schemaBuilder.build()) : Optional.empty();
  }

  /**
   * Adds list of examples to OpenAPI MediaType model
   *
   * @param mediaType The provided MediaType model
   * @param examples The list of examples
   */
  private static void getExamples(MediaType mediaType, ExampleObject[] examples) {
    if (examples.length == 1 && StringUtils.isBlank(examples[0].name())) {
      getExample(examples[0], true)
          .ifPresent((exampleObject) -> mediaType.example(exampleObject.getValue()));
    } else {
      for (ExampleObject example : examples) {
        getExample(example)
            .ifPresent((exampleObject) -> mediaType.addExamples(example.name(), exampleObject));
      }
    }
  }

  /**
   * Checks if a primitive schema is contained in the response annotation. Does not check for
   * ArraySchemas.
   *
   * @param responseAnnotation The @Response annotation
   * @return whether the response annotation's schema is primitive
   */
  private static boolean containsPrimitiveSchema(Response responseAnnotation) {
    boolean containsExample =
        responseAnnotation.examples().length > 0
            || StringUtils.isNotBlank(responseAnnotation.example());
    boolean containsDefaultImplementations =
        responseAnnotation.implementation().equals(Void.class)
            && responseAnnotation.schema().implementation().equals(Void.class);
    // check for example(s) is necessary to present false negative when implementation is not set in
    // annotation
    if (containsDefaultImplementations && containsExample) return true;
    return isImplementationPrimitive(
        responseAnnotation.implementation(), responseAnnotation.schema().implementation());
  }
}
