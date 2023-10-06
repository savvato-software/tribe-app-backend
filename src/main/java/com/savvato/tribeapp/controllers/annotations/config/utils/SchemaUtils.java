package com.savvato.tribeapp.controllers.annotations.config.utils;

import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class SchemaUtils {

  /**
   * Merge two schemas, *not* overriding any existing property in main. If you're seeing a warning
   * about unchecked calls, it's because the Schema class builder methods return a raw Schema
   * instance.
   *
   * @param main The main schema. If a property is set here, it will not be overridden by patch.
   * @param patch The patch schema. If a property is set here, it will take effect only if the main
   *     schema does not have that property set to anything
   * @return Schema The merged schema.
   */
  public static Schema<?> mergeWithoutOverride(Schema<?> main, Schema<?> patch) {
    if (main == null) {
      return patch;
    } else if (patch == null) {
      return main;
    } else {
      return new Schema<>()
          .specVersion(
              main.getSpecVersion() == null && patch.getSpecVersion() != null
                  ? patch.getSpecVersion()
                  : main.getSpecVersion())
          .contains(
              main.getContains() == null && patch.getContains() != null
                  ? patch.getContains()
                  : main.getContains())
          .$id(
              StringUtils.isBlank(main.get$id()) && !StringUtils.isBlank(patch.get$id())
                  ? patch.get$id()
                  : main.get$id())
          .$schema(
              StringUtils.isBlank(main.get$schema()) && !StringUtils.isBlank(patch.get$schema())
                  ? patch.get$schema()
                  : main.get$schema())
          .$anchor(
              StringUtils.isBlank(main.get$anchor()) && !StringUtils.isBlank(patch.get$anchor())
                  ? patch.get$anchor()
                  : main.get$anchor())
          .exclusiveMaximumValue(
              main.getExclusiveMaximumValue() != null && patch.getExclusiveMaximumValue() != null
                  ? patch.getExclusiveMaximumValue()
                  : main.getExclusiveMaximumValue())
          .exclusiveMinimumValue(
              main.getExclusiveMinimumValue() != null && patch.getExclusiveMinimumValue() != null
                  ? patch.getExclusiveMinimumValue()
                  : main.getExclusiveMinimumValue())
          .contains(
              main.getContains() == null && patch.getContains() != null
                  ? patch.getContains()
                  : main.getContains())
          .patternProperties(
              MapUtils.isEmpty(main.getPatternProperties())
                      && MapUtils.isNotEmpty(patch.getPatternProperties())
                  ? patch.getPatternProperties()
                  : main.getPatternProperties())
          .types(
              CollectionUtils.isEmpty(main.getTypes())
                      && CollectionUtils.isNotEmpty(patch.getTypes())
                  ? patch.getTypes()
                  : main.getTypes())
          .jsonSchema(
              MapUtils.isEmpty(main.getJsonSchema()) && MapUtils.isNotEmpty(patch.getJsonSchema())
                  ? patch.getJsonSchema()
                  : main.getJsonSchema())
          .allOf(
              CollectionUtils.isEmpty(main.getAllOf())
                      && CollectionUtils.isNotEmpty(patch.getAllOf())
                  ? patch.getAllOf()
                  : main.getAllOf())
          .anyOf(
              CollectionUtils.isEmpty(main.getAnyOf())
                      && CollectionUtils.isNotEmpty(patch.getAnyOf())
                  ? patch.getAnyOf()
                  : main.getAnyOf())
          .oneOf(
              CollectionUtils.isEmpty(main.getOneOf())
                      && CollectionUtils.isNotEmpty(patch.getOneOf())
                  ? patch.getOneOf()
                  : main.getOneOf())
          .items(
              main.getItems() != null && patch.getItems() == null
                  ? patch.getItems()
                  : main.getItems())
          .name(
              StringUtils.isBlank(main.getName()) && StringUtils.isNotBlank(patch.getName())
                  ? patch.getName()
                  : main.getName())
          .discriminator(
              main.getDiscriminator() == null && patch.getDiscriminator() != null
                  ? patch.getDiscriminator()
                  : main.getDiscriminator())
          .title(
              StringUtils.isBlank(main.getTitle()) && StringUtils.isNotBlank(patch.getTitle())
                  ? patch.getTitle()
                  : main.getTitle())
          ._default(
              main.getDefault() == null && patch.getDefault() != null
                  ? patch.getDefault()
                  : main.getDefault())
          ._enum(
              CollectionUtils.isEmpty(main.getEnum()) && CollectionUtils.isNotEmpty(patch.getEnum())
                  ? patch.getEnum()
                  : main.getEnum())
          .multipleOf(
              main.getMultipleOf() == null && patch.getMultipleOf() != null
                  ? patch.getMultipleOf()
                  : main.getMultipleOf())
          .minimum(
              main.getMinimum() == null && patch.getMinimum() != null
                  ? patch.getMinimum()
                  : main.getMinimum())
          .maximum(
              main.getMaximum() == null && patch.getMaximum() != null
                  ? patch.getMaximum()
                  : main.getMaximum())
          .maxLength(
              main.getMaxLength() == null && patch.getMaxLength() != null
                  ? patch.getMaxLength()
                  : main.getMaxLength())
          .minLength(
              main.getMinLength() == null && patch.getMinLength() != null
                  ? patch.getMinLength()
                  : main.getMinLength())
          .pattern(
              StringUtils.isBlank(main.getPattern()) && StringUtils.isNotBlank(patch.getPattern())
                  ? patch.getPattern()
                  : main.getPattern())
          .maxItems(
              main.getMaxItems() == null && patch.getMaxItems() != null
                  ? patch.getMaxItems()
                  : main.getMaxItems())
          .minItems(
              main.getMinItems() == null && patch.getMinItems() != null
                  ? patch.getMinItems()
                  : main.getMinItems())
          .uniqueItems(
              main.getUniqueItems() == null && patch.getUniqueItems() != null
                  ? patch.getUniqueItems()
                  : main.getUniqueItems())
          .maxProperties(
              main.getMaxProperties() == null && patch.getMaxProperties() != null
                  ? patch.getMaxItems()
                  : main.getMaxItems())
          .minProperties(
              main.getMinProperties() == null && patch.getMinProperties() != null
                  ? patch.getMinProperties()
                  : main.getMinProperties())
          .required(
              CollectionUtils.isEmpty(main.getRequired())
                      && CollectionUtils.isNotEmpty(patch.getRequired())
                  ? patch.getRequired()
                  : main.getRequired())
          .type(
              StringUtils.isBlank(main.getType()) && StringUtils.isNotBlank(patch.getType())
                  ? patch.getType()
                  : main.getType())
          .not(main.getNot() == null && patch.getNot() != null ? patch.getNot() : main.getNot())
          .properties(
              MapUtils.isEmpty(main.getProperties()) && MapUtils.isNotEmpty(patch.getProperties())
                  ? patch.getProperties()
                  : main.getProperties())
          .additionalProperties(
              main.getAdditionalProperties() == null && patch.getAdditionalProperties() != null
                  ? patch.getAdditionalProperties()
                  : main.getAdditionalProperties())
          .description(
              StringUtils.isBlank(main.getDescription())
                      && StringUtils.isNotBlank(patch.getDescription())
                  ? patch.getDescription()
                  : main.getDescription())
          .format(
              StringUtils.isBlank(main.getFormat()) && StringUtils.isNotBlank(patch.getFormat())
                  ? patch.getFormat()
                  : main.getFormat())
          .$ref(
              StringUtils.isBlank(main.get$ref()) && StringUtils.isNotBlank(patch.get$ref())
                  ? patch.get$ref()
                  : main.get$ref())
          .nullable(
              main.getNullable() == null && patch.getNullable() != null
                  ? patch.getNullable()
                  : main.getNullable())
          .readOnly(
              main.getReadOnly() == null && patch.getReadOnly() != null
                  ? patch.getReadOnly()
                  : main.getReadOnly())
          .writeOnly(
              main.getWriteOnly() == null && patch.getWriteOnly() != null
                  ? patch.getWriteOnly()
                  : main.getWriteOnly())
          .example(
              main.getExample() == null && patch.getExample() != null
                  ? patch.getExample()
                  : main.getExample())
          .externalDocs(
              main.getExternalDocs() == null && patch.getExternalDocs() != null
                  ? patch.getExternalDocs()
                  : main.getExternalDocs())
          .deprecated(
              main.getDeprecated() == null && patch.getDeprecated() != null
                  ? patch.getDeprecated()
                  : main.getDeprecated())
          .xml(main.getXml() == null && patch.getXml() != null ? patch.getXml() : main.getXml())
          .prefixItems(
              CollectionUtils.isEmpty(main.getPrefixItems())
                      && CollectionUtils.isNotEmpty(patch.getPrefixItems())
                  ? patch.getPrefixItems()
                  : main.getPrefixItems())
          .contentEncoding(
              StringUtils.isBlank(main.getContentEncoding())
                      && StringUtils.isNotBlank(patch.getContentEncoding())
                  ? patch.getContentEncoding()
                  : main.getContentEncoding())
          .contentMediaType(
              StringUtils.isBlank(main.getContentMediaType())
                      && StringUtils.isNotBlank(patch.getContentMediaType())
                  ? patch.getContentMediaType()
                  : main.getContentMediaType())
          .contentSchema(
              main.getContentSchema() == null && patch.getContentSchema() != null
                  ? patch.getContentSchema()
                  : main.getContentSchema())
          .propertyNames(
              main.getPropertyNames() == null && patch.getPropertyNames() != null
                  ? patch.getPropertyNames()
                  : main.getPropertyNames())
          .unevaluatedProperties(
              main.getUnevaluatedProperties() == null && patch.getUnevaluatedProperties() != null
                  ? patch.getUnevaluatedProperties()
                  : main.getUnevaluatedProperties())
          .maxContains(
              main.getMaxContains() == null && patch.getMaxContains() != null
                  ? patch.getMaxContains()
                  : main.getMaxContains())
          .minContains(
              main.getMinContains() == null && patch.getMinContains() != null
                  ? patch.getMinContains()
                  : main.getMinContains())
          .additionalItems(
              main.getAdditionalItems() == null && patch.getAdditionalItems() != null
                  ? patch.getAdditionalItems()
                  : main.getAdditionalItems())
          .unevaluatedItems(
              main.getUnevaluatedItems() == null && patch.getUnevaluatedItems() != null
                  ? patch.getUnevaluatedItems()
                  : main.getUnevaluatedItems())
          ._if(main.getIf() == null && patch.getIf() != null ? patch.getIf() : main.getIf())
          ._else(
              main.getElse() == null && patch.getElse() != null ? patch.getElse() : main.getElse())
          .then(
              main.getThen() == null && patch.getThen() != null ? patch.getThen() : main.getElse())
          .dependentSchemas(
              MapUtils.isEmpty(main.getDependentSchemas())
                      && MapUtils.isNotEmpty(patch.getDependentSchemas())
                  ? patch.getDependentSchemas()
                  : main.getDependentSchemas())
          .dependentRequired(
              MapUtils.isEmpty(main.getDependentRequired())
                      && MapUtils.isNotEmpty(patch.getDependentRequired())
                  ? patch.getDependentRequired()
                  : main.getDependentRequired())
          .$comment(
              StringUtils.isBlank(main.get$comment()) && StringUtils.isNotBlank(patch.get$comment())
                  ? patch.get$comment()
                  : main.get$comment())
          .examples(
              CollectionUtils.isEmpty(main.getExamples())
                      && CollectionUtils.isNotEmpty(patch.getExamples())
                  ? patch.getExamples()
                  : main.getExamples())
          .extensions(
              MapUtils.isEmpty(main.getExtensions()) && MapUtils.isNotEmpty(patch.getExtensions())
                  ? patch.getExtensions()
                  : main.getExtensions())
          ._const(
              main.getConst() == null && patch.getConst() != null
                  ? patch.getConst()
                  : main.getConst())
          .booleanSchemaValue(
              main.getBooleanSchemaValue() == null && patch.getConst() != null
                  ? patch.getBooleanSchemaValue()
                  : main.getBooleanSchemaValue())
          .exampleSetFlag(main.getExampleSetFlag() || patch.getExampleSetFlag() ? true : false);
    }
  }

  /**
   * Merge two schemas, overriding any property in main if found in patch. If you're seeing a
   * warning about unchecked calls, it's because the Schema class builder methods return a raw
   * Schema instance.
   *
   * @param main The main schema. If a property is set here, it will be overridden by patch if patch
   *     has that property.
   * @param patch The patch schema. If a property is set here, it will take effect.
   * @return Schema The merged schema.
   */
  public static Schema<?> mergeWithOverride(Schema<?> main, Schema<?> patch) {
    if (main == null) {
      return patch;
    } else if (patch == null) {
      return main;
    } else {
      return new Schema<>()
          .specVersion(
              patch.getSpecVersion() != null ? patch.getSpecVersion() : main.getSpecVersion())
          .contains(patch.getContains() != null ? patch.getContains() : main.getContains())
          .$id(!StringUtils.isBlank(patch.get$id()) ? patch.get$id() : main.get$id())
          .$schema(
              !StringUtils.isBlank(patch.get$schema()) ? patch.get$schema() : main.get$schema())
          .$anchor(
              !StringUtils.isBlank(patch.get$anchor()) ? patch.get$anchor() : main.get$anchor())
          .exclusiveMaximumValue(
              patch.getExclusiveMaximumValue() != null
                  ? patch.getExclusiveMaximumValue()
                  : main.getExclusiveMaximumValue())
          .exclusiveMinimumValue(
              patch.getExclusiveMinimumValue() != null
                  ? patch.getExclusiveMinimumValue()
                  : main.getExclusiveMinimumValue())
          .contains(patch.getContains() != null ? patch.getContains() : main.getContains())
          .patternProperties(
              MapUtils.isNotEmpty(patch.getPatternProperties())
                  ? patch.getPatternProperties()
                  : main.getPatternProperties())
          .types(CollectionUtils.isNotEmpty(patch.getTypes()) ? patch.getTypes() : main.getTypes())
          .jsonSchema(
              MapUtils.isNotEmpty(patch.getJsonSchema())
                  ? patch.getJsonSchema()
                  : main.getJsonSchema())
          .allOf(CollectionUtils.isNotEmpty(patch.getAllOf()) ? patch.getAllOf() : main.getAllOf())
          .anyOf(CollectionUtils.isNotEmpty(patch.getAnyOf()) ? patch.getAnyOf() : main.getAnyOf())
          .oneOf(CollectionUtils.isNotEmpty(patch.getOneOf()) ? patch.getOneOf() : main.getOneOf())
          .items(patch.getItems() != null ? patch.getItems() : main.getItems())
          .name(StringUtils.isNotBlank(patch.getName()) ? patch.getName() : main.getName())
          .discriminator(
              patch.getDiscriminator() != null ? patch.getDiscriminator() : main.getDiscriminator())
          .title(StringUtils.isNotBlank(patch.getTitle()) ? patch.getTitle() : main.getTitle())
          ._default(patch.getDefault() != null ? patch.getDefault() : main.getDefault())
          ._enum(CollectionUtils.isNotEmpty(patch.getEnum()) ? patch.getEnum() : main.getEnum())
          .multipleOf(patch.getMultipleOf() != null ? patch.getMultipleOf() : main.getMultipleOf())
          .minimum(patch.getMinimum() != null ? patch.getMinimum() : main.getMinimum())
          .maximum(patch.getMaximum() != null ? patch.getMaximum() : main.getMaximum())
          .maxLength(patch.getMaxLength() != null ? patch.getMaxLength() : main.getMaxLength())
          .minLength(patch.getMinLength() != null ? patch.getMinLength() : main.getMinLength())
          .pattern(
              StringUtils.isNotBlank(patch.getPattern()) ? patch.getPattern() : main.getPattern())
          .maxItems(patch.getMaxItems() != null ? patch.getMaxItems() : main.getMaxItems())
          .minItems(patch.getMinItems() != null ? patch.getMinItems() : main.getMinItems())
          .uniqueItems(
              patch.getUniqueItems() != null ? patch.getUniqueItems() : main.getUniqueItems())
          .maxProperties(
              patch.getMaxProperties() != null ? patch.getMaxItems() : main.getMaxItems())
          .minProperties(
              patch.getMinProperties() != null ? patch.getMinProperties() : main.getMinProperties())
          .required(
              CollectionUtils.isNotEmpty(patch.getRequired())
                  ? patch.getRequired()
                  : main.getRequired())
          .type(StringUtils.isNotBlank(patch.getType()) ? patch.getType() : main.getType())
          .not(patch.getNot() != null ? patch.getNot() : main.getNot())
          .properties(
              MapUtils.isNotEmpty(patch.getProperties())
                  ? patch.getProperties()
                  : main.getProperties())
          .additionalProperties(
              patch.getAdditionalProperties() != null
                  ? patch.getAdditionalProperties()
                  : main.getAdditionalProperties())
          .description(
              StringUtils.isNotBlank(patch.getDescription())
                  ? patch.getDescription()
                  : main.getDescription())
          .format(StringUtils.isNotBlank(patch.getFormat()) ? patch.getFormat() : main.getFormat())
          .$ref(StringUtils.isNotBlank(patch.get$ref()) ? patch.get$ref() : main.get$ref())
          .nullable(patch.getNullable() != null ? patch.getNullable() : main.getNullable())
          .readOnly(patch.getReadOnly() != null ? patch.getReadOnly() : main.getReadOnly())
          .writeOnly(patch.getWriteOnly() != null ? patch.getWriteOnly() : main.getWriteOnly())
          .example(patch.getExample() != null ? patch.getExample() : main.getExample())
          .externalDocs(
              patch.getExternalDocs() != null ? patch.getExternalDocs() : main.getExternalDocs())
          .deprecated(patch.getDeprecated() != null ? patch.getDeprecated() : main.getDeprecated())
          .xml(patch.getXml() != null ? patch.getXml() : main.getXml())
          .prefixItems(
              CollectionUtils.isNotEmpty(patch.getPrefixItems())
                  ? patch.getPrefixItems()
                  : main.getPrefixItems())
          .contentEncoding(
              StringUtils.isNotBlank(patch.getContentEncoding())
                  ? patch.getContentEncoding()
                  : main.getContentEncoding())
          .contentMediaType(
              StringUtils.isNotBlank(patch.getContentMediaType())
                  ? patch.getContentMediaType()
                  : main.getContentMediaType())
          .contentSchema(
              patch.getContentSchema() != null ? patch.getContentSchema() : main.getContentSchema())
          .propertyNames(
              patch.getPropertyNames() != null ? patch.getPropertyNames() : main.getPropertyNames())
          .unevaluatedProperties(
              patch.getUnevaluatedProperties() != null
                  ? patch.getUnevaluatedProperties()
                  : main.getUnevaluatedProperties())
          .maxContains(
              patch.getMaxContains() != null ? patch.getMaxContains() : main.getMaxContains())
          .minContains(
              patch.getMinContains() != null ? patch.getMinContains() : main.getMinContains())
          .additionalItems(
              patch.getAdditionalItems() != null
                  ? patch.getAdditionalItems()
                  : main.getAdditionalItems())
          .unevaluatedItems(
              patch.getUnevaluatedItems() != null
                  ? patch.getUnevaluatedItems()
                  : main.getUnevaluatedItems())
          ._if(patch.getIf() != null ? patch.getIf() : main.getIf())
          ._else(patch.getElse() != null ? patch.getElse() : main.getElse())
          .then(patch.getThen() != null ? patch.getThen() : main.getElse())
          .dependentSchemas(
              MapUtils.isNotEmpty(patch.getDependentSchemas())
                  ? patch.getDependentSchemas()
                  : main.getDependentSchemas())
          .dependentRequired(
              MapUtils.isNotEmpty(patch.getDependentRequired())
                  ? patch.getDependentRequired()
                  : main.getDependentRequired())
          .$comment(
              StringUtils.isNotBlank(patch.get$comment())
                  ? patch.get$comment()
                  : main.get$comment())
          .examples(
              CollectionUtils.isNotEmpty(patch.getExamples())
                  ? patch.getExamples()
                  : main.getExamples())
          .extensions(
              MapUtils.isNotEmpty(patch.getExtensions())
                  ? patch.getExtensions()
                  : main.getExtensions())
          ._const(patch.getConst() != null ? patch.getConst() : main.getConst())
          .booleanSchemaValue(
              patch.getConst() != null
                  ? patch.getBooleanSchemaValue()
                  : main.getBooleanSchemaValue())
          .exampleSetFlag(main.getExampleSetFlag() || patch.getExampleSetFlag() ? true : false);
    }
  }
}
