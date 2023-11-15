package com.savvato.tribeapp.controllers.annotations.utils;

import com.savvato.tribeapp.controllers.annotations.config.utils.SchemaUtils;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaUtilsTest extends SchemaUtils {
    @Autowired
    SchemaUtils schemaUtils;

    @Test
    public void mergeWithoutOverrideWhenMainNull() {
        Schema<?> main = null;
        Schema<?> patch = new ComposedSchema().name("Patch");

        Schema<?> merged = SchemaUtils.mergeWithoutOverride(main, patch);
        assertThat(patch).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithoutOverrideWhenPatchNull() {
        Schema<?> main = new ComposedSchema().name("Patch");
        Schema<?> patch = null;

        Schema<?> merged = SchemaUtils.mergeWithoutOverride(main, patch);
        assertThat(main).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithOverrideWhenMainNull() {
        Schema<?> main = null;
        Schema<?> patch = new ComposedSchema().name("Patch");

        Schema<?> merged = SchemaUtils.mergeWithOverride(main, patch);
        assertThat(patch).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithOverrideWhenPatchNull() {
        Schema<?> main = new ComposedSchema().name("Patch");
        Schema<?> patch = null;

        Schema<?> merged = SchemaUtils.mergeWithOverride(main, patch);
        assertThat(main).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithoutOverrideWhenConflict() {
        Schema<?> main =
                new Schema<>()
                        .specVersion(SpecVersion.V30)
                        .contains(new Schema<>())
                        .$id("main")
                        .$schema("mainSchema")
                        .$anchor("mainAnchor")
                        .exclusiveMaximumValue(new BigDecimal(1))
                        .exclusiveMinimumValue(new BigDecimal(0))
                        .patternProperties(new HashMap<String, Schema>(2))
                        .types(new HashSet<>(List.of("mainType")))
                        .jsonSchema(new HashMap<String, Schema>(2))
                        .allOf(new ArrayList<>(List.of(new Schema<>())))
                        .anyOf(new ArrayList<>(List.of(new Schema<>())))
                        .oneOf(new ArrayList<>(List.of(new Schema<>())))
                        .items(new Schema<>())
                        .name("main name")
                        .discriminator(new Discriminator())
                        .title("main title")
                        ._default(new Object())
                        ._enum(new ArrayList<>(List.of("main enum")))
                        .multipleOf(new BigDecimal(0))
                        .minimum(new BigDecimal(0))
                        .maximum(new BigDecimal(1))
                        .maxLength(3)
                        .minLength(0)
                        .pattern("main pattern")
                        .maxItems(3)
                        .minItems(0)
                        .uniqueItems(true)
                        .maxProperties(90)
                        .minProperties(1)
                        .required(new ArrayList<>(List.of("main required")))
                        .type("main type")
                        .not(new Schema<>())
                        .properties(new HashMap<String, Schema>(2))
                        .additionalProperties(true)
                        .description("main description")
                        .format("main format")
                        .$ref("main ref")
                        .nullable(true)
                        .readOnly(false)
                        .writeOnly(false)
                        .example(new Object())
                        .externalDocs(new ExternalDocumentation())
                        .deprecated(true)
                        .xml(new XML())
                        .prefixItems(new ArrayList<>(List.of(new Schema<>())))
                        .contentEncoding("main encoding")
                        .contentMediaType("main media type")
                        .contentSchema(new Schema<>())
                        .propertyNames(new Schema<>())
                        .unevaluatedProperties(new Schema<>())
                        .maxContains(90)
                        .minContains(1)
                        .additionalItems(new Schema<>())
                        .unevaluatedItems(new Schema<>())
                        ._if(new Schema<>())
                        ._else(new Schema<>())
                        .then(new Schema<>())
                        .dependentSchemas(new HashMap<String, Schema>(2))
                        .dependentRequired(new HashMap<String, List<String>>(2))
                        .$comment("main comment")
                        .examples(new ArrayList<>(List.of(new Object())))
                        .extensions(new HashMap<String, Object>(2))
                        ._const(new Object())
                        .booleanSchemaValue(true)
                        .exampleSetFlag(true);
        Schema<?> patch =
                new Schema<>()
                        .specVersion(SpecVersion.V31)
                        .contains(new Schema<>().name("patch"))
                        .$id("patch")
                        .$schema("patchSchema")
                        .$anchor("patchAnchor")
                        .exclusiveMaximumValue(new BigDecimal(3))
                        .exclusiveMinimumValue(new BigDecimal(1))
                        .patternProperties(new HashMap<String, Schema>(2))
                        .types(new HashSet<>(List.of("patchType")))
                        .jsonSchema(new HashMap<String, Schema>(2))
                        .allOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .anyOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .oneOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .items(new Schema<>().name("patch"))
                        .name("patch name")
                        .discriminator(new Discriminator())
                        .title("patch title")
                        ._default(new Object())
                        ._enum(new ArrayList<>(List.of("patch enum")))
                        .multipleOf(new BigDecimal(1))
                        .minimum(new BigDecimal(1))
                        .maximum(new BigDecimal(3))
                        .maxLength(3)
                        .minLength(0)
                        .pattern("patch pattern")
                        .maxItems(3)
                        .minItems(0)
                        .uniqueItems(true)
                        .maxProperties(90)
                        .minProperties(1)
                        .required(new ArrayList<>(List.of("patch required")))
                        .type("patch type")
                        .not(new Schema<>().name("patch"))
                        .properties(new HashMap<String, Schema>(2))
                        .additionalProperties(false)
                        .description("patch description")
                        .format("patch format")
                        .$ref("patch ref")
                        .nullable(true)
                        .readOnly(false)
                        .writeOnly(false)
                        .example(new Object())
                        .externalDocs(new ExternalDocumentation())
                        .deprecated(true)
                        .xml(new XML())
                        .prefixItems(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .contentEncoding("patch encoding")
                        .contentMediaType("patch media type")
                        .contentSchema(new Schema<>().name("patch"))
                        .propertyNames(new Schema<>().name("patch"))
                        .unevaluatedProperties(new Schema<>().name("patch"))
                        .maxContains(90)
                        .minContains(1)
                        .additionalItems(new Schema<>().name("patch"))
                        .unevaluatedItems(new Schema<>().name("patch"))
                        ._if(new Schema<>().name("patch"))
                        ._else(new Schema<>().name("patch"))
                        .then(new Schema<>().name("patch"))
                        .dependentSchemas(new HashMap<String, Schema>(2))
                        .dependentRequired(new HashMap<String, List<String>>(2))
                        .$comment("patch comment")
                        .examples(new ArrayList<>(List.of(new Object())))
                        .extensions(new HashMap<String, Object>(2))
                        ._const(new Object())
                        .booleanSchemaValue(true)
                        .exampleSetFlag(true);
        Schema<?> merged = SchemaUtils.mergeWithoutOverride(main, patch);
        assertThat(main).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithoutOverrideWhenBothSchemasAreDefaultSchema() {

        Schema<?> main = new Schema<>();

        Schema<?> patch = new Schema<>();
        Schema<?> merged = SchemaUtils.mergeWithoutOverride(main, patch);
        assertThat(main).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithoutOverrideWhenNoConflictingPropertiesInMain() {
        Schema<?> main = new Schema<>();
        Schema<?> patch =
                new Schema<>()
                        .contains(new Schema<>().name("patch"))
                        .$id("patch")
                        .$schema("patchSchema")
                        .$anchor("patchAnchor")
                        .exclusiveMaximumValue(new BigDecimal(3))
                        .exclusiveMinimumValue(new BigDecimal(1))
                        .patternProperties(Map.of("patch properties", new Schema<>()))
                        .types(new HashSet<>(List.of("patch types")))
                        .jsonSchema(Map.of("patch json schema", new Schema<>()))
                        .allOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .anyOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .oneOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .items(new Schema<>().name("patch"))
                        .name("patch name")
                        .discriminator(new Discriminator())
                        .title("patch title")
                        ._default(new Object())
                        ._enum(new ArrayList<>(List.of("patch enum")))
                        .multipleOf(new BigDecimal(1))
                        .minimum(new BigDecimal(1))
                        .maximum(new BigDecimal(3))
                        .maxLength(3)
                        .minLength(0)
                        .pattern("patch pattern")
                        .maxItems(3)
                        .minItems(0)
                        .uniqueItems(true)
                        .maxProperties(90)
                        .minProperties(1)
                        .required(new ArrayList<>(List.of("patch required")))
                        .type("patch type")
                        .not(new Schema<>().name("patch not"))
                        .properties(Map.of("patch properties", new Schema<>()))
                        .additionalProperties(false)
                        .description("patch description")
                        .format("patch format")
                        .$ref("patch ref")
                        .nullable(true)
                        .readOnly(false)
                        .writeOnly(false)
                        .example(new Object())
                        .externalDocs(new ExternalDocumentation())
                        .deprecated(true)
                        .xml(new XML())
                        .prefixItems(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .contentEncoding("patch encoding")
                        .contentMediaType("patch media type")
                        .contentSchema(new Schema<>().name("patch"))
                        .propertyNames(new Schema<>().name("patch"))
                        .unevaluatedProperties(new Schema<>().name("patch"))
                        .maxContains(90)
                        .minContains(1)
                        .additionalItems(new Schema<>().name("patch"))
                        .unevaluatedItems(new Schema<>().name("patch"))
                        ._if(new Schema<>().name("patch"))
                        ._else(new Schema<>().name("patch"))
                        .then(new Schema<>().name("patch"))
                        .dependentSchemas(Map.of("patch dependents", new Schema<>()))
                        .dependentRequired(
                                Map.of("patch required", new ArrayList<>(List.of("patch required 1"))))
                        .$comment("patch comment")
                        .examples(new ArrayList<>(List.of(new Object())))
                        .extensions(Map.of("patch", "patch extension"))
                        ._const(new Object())
                        .booleanSchemaValue(true)
                        .exampleSetFlag(true);
        Schema<?> merged = SchemaUtils.mergeWithoutOverride(main, patch);
        assertThat(patch).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithOverrideWhenConflict() {
        Schema<?> main =
                new Schema<>()
                        .specVersion(SpecVersion.V30)
                        .contains(new Schema<>())
                        .$id("main")
                        .$schema("mainSchema")
                        .$anchor("mainAnchor")
                        .exclusiveMaximumValue(new BigDecimal(1))
                        .exclusiveMinimumValue(new BigDecimal(0))
                        .patternProperties(new HashMap<String, Schema>(2))
                        .types(new HashSet<>(List.of("mainType")))
                        .jsonSchema(new HashMap<String, Schema>(2))
                        .allOf(new ArrayList<>(List.of(new Schema<>())))
                        .anyOf(new ArrayList<>(List.of(new Schema<>())))
                        .oneOf(new ArrayList<>(List.of(new Schema<>())))
                        .items(new Schema<>())
                        .name("main name")
                        .discriminator(new Discriminator())
                        .title("main title")
                        ._default(new Object())
                        ._enum(new ArrayList<>(List.of("main enum")))
                        .multipleOf(new BigDecimal(0))
                        .minimum(new BigDecimal(0))
                        .maximum(new BigDecimal(1))
                        .maxLength(3)
                        .minLength(0)
                        .pattern("main pattern")
                        .maxItems(3)
                        .minItems(0)
                        .uniqueItems(true)
                        .maxProperties(90)
                        .minProperties(1)
                        .required(new ArrayList<>(List.of("main required")))
                        .type("main type")
                        .not(new Schema<>())
                        .properties(new HashMap<String, Schema>(2))
                        .additionalProperties(true)
                        .description("main description")
                        .format("main format")
                        .$ref("main ref")
                        .nullable(true)
                        .readOnly(false)
                        .writeOnly(false)
                        .example(new Object())
                        .externalDocs(new ExternalDocumentation())
                        .deprecated(true)
                        .xml(new XML())
                        .prefixItems(new ArrayList<>(List.of(new Schema<>())))
                        .contentEncoding("main encoding")
                        .contentMediaType("main media type")
                        .contentSchema(new Schema<>())
                        .propertyNames(new Schema<>())
                        .unevaluatedProperties(new Schema<>())
                        .maxContains(90)
                        .minContains(1)
                        .additionalItems(new Schema<>())
                        .unevaluatedItems(new Schema<>())
                        ._if(new Schema<>())
                        ._else(new Schema<>())
                        .then(new Schema<>())
                        .dependentSchemas(new HashMap<String, Schema>(2))
                        .dependentRequired(new HashMap<String, List<String>>(2))
                        .$comment("main comment")
                        .examples(new ArrayList<>(List.of(new Object())))
                        .extensions(new HashMap<String, Object>(2))
                        ._const(new Object())
                        .booleanSchemaValue(true)
                        .exampleSetFlag(true);
        Schema<?> patch =
                new Schema<>()
                        .specVersion(SpecVersion.V31)
                        .contains(new Schema<>().name("patch"))
                        .$id("patch")
                        .$schema("patchSchema")
                        .$anchor("patchAnchor")
                        .exclusiveMaximumValue(new BigDecimal(3))
                        .exclusiveMinimumValue(new BigDecimal(1))
                        .patternProperties(new HashMap<String, Schema>(2))
                        .types(new HashSet<>(List.of("patchType")))
                        .jsonSchema(new HashMap<String, Schema>(2))
                        .allOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .anyOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .oneOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .items(new Schema<>().name("patch"))
                        .name("patch name")
                        .discriminator(new Discriminator())
                        .title("patch title")
                        ._default(new Object())
                        ._enum(new ArrayList<>(List.of("patch enum")))
                        .multipleOf(new BigDecimal(1))
                        .minimum(new BigDecimal(1))
                        .maximum(new BigDecimal(3))
                        .maxLength(3)
                        .minLength(0)
                        .pattern("patch pattern")
                        .maxItems(3)
                        .minItems(0)
                        .uniqueItems(true)
                        .maxProperties(90)
                        .minProperties(1)
                        .required(new ArrayList<>(List.of("patch required")))
                        .type("patch type")
                        .not(new Schema<>().name("patch"))
                        .properties(new HashMap<String, Schema>(2))
                        .additionalProperties(false)
                        .description("patch description")
                        .format("patch format")
                        .$ref("patch ref")
                        .nullable(true)
                        .readOnly(false)
                        .writeOnly(false)
                        .example(new Object())
                        .externalDocs(new ExternalDocumentation())
                        .deprecated(true)
                        .xml(new XML())
                        .prefixItems(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .contentEncoding("patch encoding")
                        .contentMediaType("patch media type")
                        .contentSchema(new Schema<>().name("patch"))
                        .propertyNames(new Schema<>().name("patch"))
                        .unevaluatedProperties(new Schema<>().name("patch"))
                        .maxContains(90)
                        .minContains(1)
                        .additionalItems(new Schema<>().name("patch"))
                        .unevaluatedItems(new Schema<>().name("patch"))
                        ._if(new Schema<>().name("patch"))
                        ._else(new Schema<>().name("patch"))
                        .then(new Schema<>().name("patch"))
                        .dependentSchemas(new HashMap<String, Schema>(2))
                        .dependentRequired(new HashMap<String, List<String>>(2))
                        .$comment("patch comment")
                        .examples(new ArrayList<>(List.of(new Object())))
                        .extensions(new HashMap<String, Object>(2))
                        ._const(new Object())
                        .booleanSchemaValue(true)
                        .exampleSetFlag(true);
        Schema<?> merged = SchemaUtils.mergeWithOverride(main, patch);
        assertThat(patch).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithOverrideWhenNoConflictingPropertiesInMain() {
        Schema<?> patch = new Schema<>();
        Schema<?> main =
                new Schema<>()
                        .contains(new Schema<>().name("patch"))
                        .$id("patch")
                        .$schema("patchSchema")
                        .$anchor("patchAnchor")
                        .exclusiveMaximumValue(new BigDecimal(3))
                        .exclusiveMinimumValue(new BigDecimal(1))
                        .patternProperties(Map.of("patch properties", new Schema<>()))
                        .types(new HashSet<>(List.of("patch types")))
                        .jsonSchema(Map.of("patch json schema", new Schema<>()))
                        .allOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .anyOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .oneOf(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .items(new Schema<>().name("patch"))
                        .name("patch name")
                        .discriminator(new Discriminator())
                        .title("patch title")
                        ._default(new Object())
                        ._enum(new ArrayList<>(List.of("patch enum")))
                        .multipleOf(new BigDecimal(1))
                        .minimum(new BigDecimal(1))
                        .maximum(new BigDecimal(3))
                        .maxLength(3)
                        .minLength(0)
                        .pattern("patch pattern")
                        .maxItems(3)
                        .minItems(0)
                        .uniqueItems(true)
                        .maxProperties(90)
                        .minProperties(1)
                        .required(new ArrayList<>(List.of("patch required")))
                        .type("patch type")
                        .not(new Schema<>().name("patch not"))
                        .properties(Map.of("patch properties", new Schema<>()))
                        .additionalProperties(false)
                        .description("patch description")
                        .format("patch format")
                        .$ref("patch ref")
                        .nullable(true)
                        .readOnly(false)
                        .writeOnly(false)
                        .example(new Object())
                        .externalDocs(new ExternalDocumentation())
                        .deprecated(true)
                        .xml(new XML())
                        .prefixItems(new ArrayList<>(List.of(new Schema<>().name("patch"))))
                        .contentEncoding("patch encoding")
                        .contentMediaType("patch media type")
                        .contentSchema(new Schema<>().name("patch"))
                        .propertyNames(new Schema<>().name("patch"))
                        .unevaluatedProperties(new Schema<>().name("patch"))
                        .maxContains(90)
                        .minContains(1)
                        .additionalItems(new Schema<>().name("patch"))
                        .unevaluatedItems(new Schema<>().name("patch"))
                        ._if(new Schema<>().name("patch"))
                        ._else(new Schema<>().name("patch"))
                        .then(new Schema<>().name("patch"))
                        .dependentSchemas(Map.of("patch dependents", new Schema<>()))
                        .dependentRequired(
                                Map.of("patch required", new ArrayList<>(List.of("patch required 1"))))
                        .$comment("patch comment")
                        .examples(new ArrayList<>(List.of(new Object())))
                        .extensions(Map.of("patch", "patch extension"))
                        ._const(new Object())
                        .booleanSchemaValue(true)
                        .exampleSetFlag(true);
        Schema<?> merged = SchemaUtils.mergeWithOverride(main, patch);
        assertThat(main).usingRecursiveComparison().isEqualTo(merged);
    }

    @Test
    public void mergeWithoutOverride() {
        Schema<?> main = new Schema<>();
        Schema<?> patch = new Schema<>();
    }
}
