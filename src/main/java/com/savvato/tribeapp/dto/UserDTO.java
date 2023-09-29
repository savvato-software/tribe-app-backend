package com.savvato.tribeapp.dto;

import lombok.Builder;

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
}
