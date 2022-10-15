package com.savvato.tribeapp.dto;

import lombok.Builder;

@Builder
public class ProfileDTO {

	public String name;
	public String email;
	public String phone;
	public String created;
	public String lastUpdated;
}
