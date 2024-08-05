package com.community.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Community REST API Specifications")
                .description("This API provides essential features for the community site, supporting user management, post creation, comment functionality, and various community activities.")
                .version("1.0.0");
    }
    
    @Bean
    public OperationCustomizer customOperation() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
        	// @RestController가 붙지 않은 메서드는 Swagger 문서에서 제외
            return handlerMethod.getBeanType().isAnnotationPresent(RestController.class)
            		? operation
            		: null;
        };
    }
}
