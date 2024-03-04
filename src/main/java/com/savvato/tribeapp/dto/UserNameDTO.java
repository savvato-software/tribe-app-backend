package com.savvato.tribeapp.dto;

import com.savvato.tribeapp.entities.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

@Builder
public class UserNameDTO {
  @Schema(example = "1")
  public Long userId;

  @Schema(example = "John Doe")
  public String userName;

}
