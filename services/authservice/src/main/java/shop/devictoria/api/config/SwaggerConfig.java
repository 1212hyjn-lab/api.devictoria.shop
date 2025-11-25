package shop.devictoria.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Auth Service")
						.description("Devictoria Shop Authentication Service")
						.version("1.0.0")
						.contact(new Contact()
								.name("Devictoria Shop")
								.email("support@devictoria.shop")));
	}
}

