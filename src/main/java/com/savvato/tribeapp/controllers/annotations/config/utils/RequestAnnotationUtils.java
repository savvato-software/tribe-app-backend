package com.savvato.tribeapp.controllers.annotations.config.utils;

import com.savvato.tribeapp.controllers.annotations.config.builders.SchemaBuilder;
import com.savvato.tribeapp.controllers.annotations.requests.DocumentedRequestBody;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.MediaType;
import java.util.Optional;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.fn.builders.schema.Builder;

@Generated
public class RequestAnnotationUtils extends AnnotationsUtils {
  private static final Builder SchemaAnnotationBuilder = Builder.schemaBuilder();

  /**
   * Gets OpenAPI Content model from @DocumentedRequestBody annotation
   *
   * @param requestBodyAnnotation The @DocumentedRequestBody annotation
   * @return the corresponding OpenAPI content model
   */
  public static Optional<io.swagger.v3.oas.models.media.Content> getContent(
      DocumentedRequestBody requestBodyAnnotation) {
    if (requestBodyAnnotation.schema().hidden()) return Optional.empty();
    MediaType mediaType = getMediaType(requestBodyAnnotation);
    io.swagger.v3.oas.models.media.Content content = new io.swagger.v3.oas.models.media.Content();
    content.addMediaType(
        containsPrimitiveSchema(requestBodyAnnotation) ? "*/*" : "application/json", mediaType);
    return Optional.of(content);
  }

  /**
   * Gets description from @DocumentedRequestBody annotation
   *
   * @param requestBodyAnnotation The @DocumentedRequestBody annotation
   * @return the description wrapped in an Optional
   */
  public static Optional<String> getDescription(DocumentedRequestBody requestBodyAnnotation) {
    if (StringUtils.isNotBlank(requestBodyAnnotation.description())) {
      return Optional.of(requestBodyAnnotation.description());
    }

    return Optional.empty();
  }

  /**
   * Gets OpenAPI MediaType model from @DocumentedRequestBody annotation
   *
   * @param requestBodyAnnotation - The @DocumentedRequestBody annotation
   * @return the corresponding OpenAPI MediaType model
   */
  private static MediaType getMediaType(DocumentedRequestBody requestBodyAnnotation) {
    MediaType mediaType = new MediaType();
    getSchema(requestBodyAnnotation).ifPresent(mediaType::schema);
    return mediaType;
  }

  /**
   * Gets OpenAPI schema model from a @DocumentedRequestBody annotation. Includes array (if present)
   * and custom schema (if present) in the model returned.
   *
   * @param requestBodyAnnotation The @DocumentedRequestBody annotation
   * @return the corresponding OpenAPI schema model
   */
  private static Optional<io.swagger.v3.oas.models.media.Schema<?>> getSchema(
      DocumentedRequestBody requestBodyAnnotation) {
    SchemaBuilder schemaBuilder = SchemaBuilder.builder();
    getSchemaFromRequestBodyAnnotation(requestBodyAnnotation).ifPresent(schemaBuilder::merge);
    getSchemaFromSchemaAnnotation(requestBodyAnnotation.schema()).ifPresent(schemaBuilder::merge);
    getArraySchemaFromAnnotation(requestBodyAnnotation.array()).ifPresent(schemaBuilder::merge);
    return (schemaBuilder.build() != null) ? Optional.of(schemaBuilder.build()) : Optional.empty();
  }

  /**
   * Gets schema model from @DocumentedRequestBody annotation, <strong>assuming no custom schema or
   * array is present.</strong>
   *
   * @param requestBodyAnnotation The @DocumentedRequestBody annotation
   * @return The corresponding OpenAPI schema model
   */
  private static Optional<io.swagger.v3.oas.models.media.Schema<?>>
      getSchemaFromRequestBodyAnnotation(DocumentedRequestBody requestBodyAnnotation) {
    /*
     Creating two schemas and merging them is necessary because the schema produced by resolving
     implementation() does not contain any specified non-implementation properties, such as title.
    */
    Schema nonImplementationSchemaAnnotation =
        SchemaAnnotationBuilder.example(requestBodyAnnotation.example()).build();
    // TODO: see if this can be improved - currently it fires even if no non-implementation
    // properties exist
    SchemaBuilder schemaBuilder = SchemaBuilder.builder();
    resolveSchemaFromType(requestBodyAnnotation.implementation()).ifPresent(schemaBuilder::merge);
    getSchemaFromAnnotation(nonImplementationSchemaAnnotation, null)
        .ifPresent(schemaBuilder::merge);

    return (schemaBuilder.build() != null) ? Optional.of(schemaBuilder.build()) : Optional.empty();
  }

  /**
   * Checks if a primitive schema is contained in the @DocumentedRequestBody annotation. Does not
   * check for ArraySchemas.
   *
   * @param requestBodyAnnotation The @DocumentedRequestBody annotation
   * @return whether the @DocumentedRequestBody annotation's schema is primitive
   */
  private static boolean containsPrimitiveSchema(DocumentedRequestBody requestBodyAnnotation) {

    boolean containsExample = StringUtils.isNotBlank(requestBodyAnnotation.example());
    boolean containsDefaultImplementations =
        requestBodyAnnotation.implementation().equals(Void.class)
            && requestBodyAnnotation.schema().implementation().equals(Void.class);
    // check for example(s) is necessary to present false negative when implementation is not set in
    // annotation
    if (containsDefaultImplementations && containsExample) return true;
    return isImplementationPrimitive(
        requestBodyAnnotation.implementation(), requestBodyAnnotation.schema().implementation());
  }
}
