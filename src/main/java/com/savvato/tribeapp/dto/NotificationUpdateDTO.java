package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A notification DTO")
public class NotificationUpdateDTO {
  @Schema(example = "true", implementation = Boolean.class)
  private boolean isRead;
}
