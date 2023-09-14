package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "NotificationDTO", description = "A notification DTO")
public class NotificationDTO {
  public long id;

  @Schema(example = "ATTRIBUTE_REQUEST_REJECTED")
  public String description;

  @Schema(
      example =
          "Your attribute was rejected. This attribute is unsuitable and cannot be applied to users.",
      required = true)
  public String body;

  @Schema(example = "2007-12-03T10:15:30")
  public String lastUpdatedDate;

  @Schema(example = "https://www.google.com")
  public String iconUrl;

  public boolean isRead;
}
