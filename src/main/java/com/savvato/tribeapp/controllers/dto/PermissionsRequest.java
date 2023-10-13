package com.savvato.tribeapp.controllers.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;

@Schema(description = "A request containing a list of roles")
public class PermissionsRequest {

  @Schema(example = "1")
  public Long id;

  @ArraySchema(
      schema =
          @Schema(
              required = true,
              allowableValues = {"ACCOUNTHOLDER", "ADMIN", "PHRASE_REVIEWER"}))
  public ArrayList<String> permissions;
}
