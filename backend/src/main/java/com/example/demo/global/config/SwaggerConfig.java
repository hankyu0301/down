package com.example.demo.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                .title("Down API Server V1")
                .description("인프런 팀프로젝트 Down의 V1 API 명세서입니다.")
                .version("v1.0.1")
                .contact(new Contact()
                        .name("인프런 팀 Down")
                        .email("준비중@inflearn.com"))
                .license(new License()
                        .name("Apache License Version 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

}