package com.savvato.tribeapp.controllers.annotations.config.utils;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Generated
public class AnnotationsUtils extends io.swagger.v3.core.util.AnnotationsUtils {

  /**
   * Gets OpenAPI schema model from @Schema annotation. Assumes no arrays involved.
   *
   * @param schema The @Schema annotation
   * @return the corresponding OpenAPI schema model
   */
  public static Optional<io.swagger.v3.oas.models.media.Schema<?>> getSchemaFromSchemaAnnotation(
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
  public static Optional<io.swagger.v3.oas.models.media.ArraySchema> getArraySchemaFromAnnotation(
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
  public static Optional<io.swagger.v3.oas.models.media.Schema<?>> resolveSchemaFromType(
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
   * Checks if @Schema annotation has non-implementation properties. Necessary because the schema
   * produced by resolving implementation() does not contain any specified non-implementation
   * properties, such as title.
   *
   * @param schema The @Schema annotation
   * @return whether the custom schema annotation has such overrides
   */
  private static boolean hasNonImplementationOverrides(Schema schema) {
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
        || !schema.requiredMode().equals(Schema.RequiredMode.AUTO)
        || schema.nullable()
        || schema.readOnly()
        || schema.writeOnly()
        || !schema.accessMode().equals(Schema.AccessMode.AUTO)
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
            .equals(Schema.AdditionalPropertiesValue.USE_ADDITIONAL_PROPERTIES_ANNOTATION);
  }

  protected static boolean isImplementationPrimitive(Class<?>... implementations) {
    return Arrays.stream(implementations).map(PrimitiveType::fromType).anyMatch(Objects::nonNull);
  }
}
