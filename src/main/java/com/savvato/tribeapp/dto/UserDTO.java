package com.savvato.tribeapp.dto;

import com.savvato.tribeapp.entities.UserRole;
import lombok.Builder;

import java.util.Set;

@Builder
public class UserDTO {
    public Long id;
    public String name;
    public String password;
    public String phone;
    public String email;
    public Integer enabled;
    public String created;
    public String lastUpdated;
    public Set<UserRole> roles;
}
