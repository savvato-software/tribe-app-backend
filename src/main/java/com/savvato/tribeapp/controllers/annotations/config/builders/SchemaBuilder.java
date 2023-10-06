package com.savvato.tribeapp.controllers.annotations.config.builders;

import com.savvato.tribeapp.controllers.annotations.config.utils.SchemaUtils;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.NoArgsConstructor;

/** Builder class for OpenAPI schema model. */
@NoArgsConstructor
public class SchemaBuilder extends ComposedSchema {
  private Schema<?> schema;

  private Schema<?> getSchema() {
    return this.schema;
  }

  private void setSchema(Schema<?> newSchema) {
    schema = newSchema;
  }

  public Schema<?> build() {
    if (schema != null && schema.getType() == null) schema.type("string");
    return schema;
  }

  public void merge(Schema<?> patch) {
    setSchema(SchemaUtils.mergeWithOverride(schema, patch));
  }

  public static SchemaBuilder builder() {
    return new SchemaBuilder();
  }
}
