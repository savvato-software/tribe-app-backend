package com.savvato.tribeapp.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;

@Builder
@JsonDeserialize(builder = UserRoleDTO.UserRoleDTOBuilder.class)
public class UserRoleDTO {
    public String name;
    public Long id;
}
