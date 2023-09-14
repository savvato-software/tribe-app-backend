package com.savvato.tribeapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class ProfileDTO {
	@Schema(example= "John Doe", required=true)
	public String name;
	@Schema(example= "johndoe@gmail.com", required=true)
	public String email;
	@Schema(example= "1234567890", required=true)
	public String phone;
	@Schema(example= "2007-12-03T10:15:30", required=true)
	public String created;
	@Schema(example= "2007-12-13T10:15:30", required=true)
	public String lastUpdated;
}
