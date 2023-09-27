package com.savvato.tribeapp.config;

import io.swagger.v3.core.util.AnnotationsUtils;
import org.reflections.Reflections;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.reflections.scanners.Scanners;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Ensures that all relevant schemas show up in OpenAPI documentation, by scanning certain packages.
 * A distinction is made between packages whose classes must always be modeled (such as those in the
 * DTO directories) and those whose classes would be modeled only if they have a @Schema annotation.
 */
@Configuration
public class OpenApiSchemaConfig {
  private final String[] modelIfSchemaAnnotationPresent = {"com.savvato.tribeapp.entities"};
  private final String[] alwaysModel = {
    "com.savvato.tribeapp.controllers.dto", "com.savvato.tribeapp.dto"
  };

  @Bean
  public OpenApiCustomiser addAllSchemasToSwagger() {
    return openApi -> {
      for (Class<?> schemaClass : getClassesToScan()) {

        Schema schema = (Schema) schemaClass.getAnnotation(Schema.class);
        String name = schema != null ? schema.name() : schemaClass.getSimpleName();

        if (!openApi.getComponents().getSchemas().containsKey(name)) {
          ResolvedSchema resolvedSchema =
              ModelConverters.getInstance().readAllAsResolvedSchema(schemaClass);
          if (resolvedSchema.schema.getName() != null)
            openApi.schema(resolvedSchema.schema.getName(), resolvedSchema.schema);
        }
      }
    };
  }

  private Set<Class<?>> getClassesToScan() {
    Set<Class<?>> allClasses = new HashSet<>();
    allClasses.addAll(getClassesWithSchemaAnnotation());
    allClasses.addAll(getAllClasses());
    return allClasses;
  }

  private Set<Class<?>> getAllClasses() {
    Scanners scanner = Scanners.SubTypes.filterResultsBy(c -> true);
    return Arrays.stream(alwaysModel)
        .map(prefix -> new Reflections(prefix, scanner).getSubTypesOf(Object.class))
        .flatMap(Set::stream)
        .filter(
            classToScan ->
                classToScan.getEnclosingClass()
                    == null) // filter out Lombok-generated Builder classes
        .collect(Collectors.toSet());
  }

  private Set<Class<?>> getClassesWithSchemaAnnotation() {
    Scanners scanner = Scanners.SubTypes.filterResultsBy(c -> true);
    return Arrays.stream(modelIfSchemaAnnotationPresent)
        .map(prefix -> new Reflections(prefix, scanner).getSubTypesOf(Object.class))
        .flatMap(Set::stream)
        .filter(classToScan -> AnnotationsUtils.getSchemaAnnotation(classToScan) != null)
        .collect(Collectors.toSet());
  }
}
