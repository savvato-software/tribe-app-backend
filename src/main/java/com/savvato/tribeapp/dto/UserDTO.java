package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import com.savvato.tribeapp.entities.UserRole;

import lombok.Builder;

import java.util.Set;

@Builder
public class UserDTO {
  @Schema(example = "1")
  public Long id;

  @Schema(example = "John Doe")
  public String name;

  @Schema(example = "admin")
  public String password;

  @Schema(example = "1234567890")
  public String phone;

  @Schema(example = "admin@tribeapp.com")
  public String email;

  @Schema(example = "1")
  public Integer enabled;

  @Schema(example = "2007-12-03T10:15:30")
  public String created;

  @Schema(example = "2007-12-13T10:15:30")
  public String lastUpdated;

  public Set<UserRole> roles;
}
