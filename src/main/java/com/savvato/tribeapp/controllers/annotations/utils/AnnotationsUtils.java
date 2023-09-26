package com.savvato.tribeapp.controllers.annotations.utils;

import com.savvato.tribeapp.controllers.annotations.builders.SchemaBuilder;
import com.savvato.tribeapp.controllers.annotations.responses.Response;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.fn.builders.schema.Builder;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class AnnotationsUtils extends io.swagger.v3.core.util.AnnotationsUtils {
  private static final org.springdoc.core.fn.builders.schema.Builder SchemaAnnotationBuilder =
      Builder.schemaBuilder();

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
    if (responseAnnotation.implementation() == Void.class
        && responseAnnotation.schema().implementation() == Void.class) {
      var a = 1;
    }
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
    io.swagger.v3.oas.annotations.media.Schema nonImplementationSchemaAnnotation =
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
   * Gets OpenAPI schema model from @Schema annotation. Assumes no arrays involved.
   *
   * @param schema The @Schema annotation
   * @return the corresponding OpenAPI schema model
   */
  private static Optional<io.swagger.v3.oas.models.media.Schema<?>> getSchemaFromSchemaAnnotation(
      Schema schema) {
    io.swagger.v3.oas.models.media.Schema<?> resolvedSchema;
    io.swagger.v3.oas.models.media.Schema<?> nonImplementationSchema;
    if (hasSchemaAnnotation(schema)) {
      resolvedSchema = resolveSchemaFromType(schema.implementation()).orElse(null);
      if (hasNonImplementationOverrides(schema)) {
        nonImplementationSchema = getSchemaFromAnnotation(schema, null).orElse(null);
        return Optional.of(SchemaUtils.mergeWithOverride(resolvedSchema, nonImplementationSchema));
      }
      return Optional.of(resolvedSchema);
    }
    return Optional.empty();
  }

  /**
   * Gets OpenAPI ArraySchema model from ArraySchema annotation.
   *
   * @param array The @ArraySchema annotation
   * @return the corresponding OpenAPI ArraySchema model
   */
  private static Optional<io.swagger.v3.oas.models.media.ArraySchema> getArraySchemaFromAnnotation(
      ArraySchema array) {
    Optional<io.swagger.v3.oas.models.media.ArraySchema> opt = getArraySchema(array, null);
    opt.ifPresent(
        arrayModel -> getSchemaFromSchemaAnnotation(array.schema()).ifPresent(arrayModel::items));

    return opt;
  }

  /**
   * Resolves OpenAPI Schema model from a class
   *
   * @param implementation the class which needs to be modelled
   * @return the resolved Schema model
   */
  private static Optional<io.swagger.v3.oas.models.media.Schema<?>> resolveSchemaFromType(
      Class<?> implementation) {
    if (!implementation.equals(Void.class)) {
      io.swagger.v3.oas.models.media.Schema<?> resolvedSchema =
          ModelConverters.getInstance()
              .resolveAsResolvedSchema(new AnnotatedType(implementation))
              .schema;
      if (StringUtils.isBlank(resolvedSchema.getTitle()))
        resolvedSchema.title(resolvedSchema.getName());

      return Optional.of(resolvedSchema);
    }
    return Optional.empty();
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
   * Checks if @Schema annotation has non-implementation properties. Necessary because the schema
   * produced by resolving implementation() does not contain any specified non-implementation
   * properties, such as title.
   *
   * @param schema The @Schema annotation
   * @return whether the custom schema annotation has such overrides
   */
  private static boolean hasNonImplementationOverrides(
      io.swagger.v3.oas.annotations.media.Schema schema) {
    return !StringUtils.isBlank(schema.format())
        || !StringUtils.isBlank(schema.title())
        || !StringUtils.isBlank(schema.description())
        || !StringUtils.isBlank(schema.name())
        || schema.multipleOf() != 0.0
        || !StringUtils.isBlank(schema.maximum())
        || !StringUtils.isBlank(schema.minimum())
        || schema.exclusiveMinimum()
        || schema.exclusiveMaximum()
        || schema.maxLength() != Integer.MAX_VALUE
        || schema.minLength() != 0
        || schema.minProperties() != 0
        || schema.maxProperties() != 0
        || schema.requiredProperties().length != 0
        || schema.required()
        || !schema
            .requiredMode()
            .equals(io.swagger.v3.oas.annotations.media.Schema.RequiredMode.AUTO)
        || schema.nullable()
        || schema.readOnly()
        || schema.writeOnly()
        || !schema.accessMode().equals(io.swagger.v3.oas.annotations.media.Schema.AccessMode.AUTO)
        || schema.deprecated()
        || schema.allowableValues().length != 0
        || !StringUtils.isBlank(schema.defaultValue())
        || !StringUtils.isBlank(schema.example())
        || !StringUtils.isBlank(schema.pattern())
        || !schema.not().equals(Void.class)
        || schema.allOf().length != 0
        || schema.oneOf().length != 0
        || schema.anyOf().length != 0
        || schema.subTypes().length != 0
        || getExternalDocumentation(schema.externalDocs()).isPresent()
        || !StringUtils.isBlank(schema.discriminatorProperty())
        || schema.discriminatorMapping().length != 0
        || schema.extensions().length != 0
        || schema.hidden()
        || schema.enumAsRef()
        || !schema
            .additionalProperties()
            .equals(
                io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue
                    .USE_ADDITIONAL_PROPERTIES_ANNOTATION);
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

  private static boolean isImplementationPrimitive(Class<?>... implementations) {
    return Arrays.stream(implementations).map(PrimitiveType::fromType).anyMatch(Objects::nonNull);
  }

  public static boolean equals(Content thisContent, Content thatContent) {
    if (thisContent == null && thatContent == null) {
      return true;
    } else if (thisContent != null && thatContent != null) {
      if (!equals(thisContent.array(), thatContent.array())) {
        return false;
      } else if (!Arrays.equals(thisContent.examples(), thisContent.examples())) {
        return false;
      } else if (!StringUtils.equals(thisContent.mediaType(), thatContent.mediaType())) {
        return false;
      } else if (!Arrays.equals(thisContent.encoding(), thatContent.encoding())) {
        return false;
      } else if (!Arrays.equals(thisContent.extensions(), thisContent.extensions())) {
        return false;
      } else if (!Arrays.equals(thisContent.schemaProperties(), thisContent.schemaProperties())) {
        return false;
      } else if (!equals(thisContent.schema(), thatContent.schema())) {
        return false;
      } else {
        return equals(
            thisContent.additionalPropertiesSchema(), thatContent.additionalPropertiesSchema());
      }
    } else {
      return false;
    }
  }
}
