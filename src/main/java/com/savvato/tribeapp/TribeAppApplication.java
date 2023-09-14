package com.savvato.tribeapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "TribeApp Backend",
				version = "1.0.0",
				description = "TribeApp Backend",
				license= @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
				contact=@Contact(email="me.myAPI@test.com")
		),
		servers = { @Server(
				description = "Development server",
				url = "")    },
		security = {
				@SecurityRequirement(name = "Bearer Authentication")}
)
public class TribeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TribeAppApplication.class, args);
	}
}
