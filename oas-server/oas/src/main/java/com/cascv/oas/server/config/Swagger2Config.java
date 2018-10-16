package com.cascv.oas.server.config;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger2Config {

	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
					.apiInfo(apiInfo())
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.cascv.oas"))
					.paths(PathSelectors.any())
					.build();
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("OAS API").description("for phase 1").version("1.0").build();
	}
}
