package br.com.willian.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Hello Swagger OpenAPI")
						.version("V1")
						.description("Some Description about your API.")
						.termsOfService("https://pub.willian.com.br/API")
						.license(
								new License()
									.name("Apache 2.0")
									.url("https://pub.willian.com.br/API")
						));
	}
}
