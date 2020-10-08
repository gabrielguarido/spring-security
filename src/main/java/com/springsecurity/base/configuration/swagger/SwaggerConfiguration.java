package com.springsecurity.base.configuration.swagger;

import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Configure Swagger 2 documentation.
 *
 * @author Gabriel Oliveira
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends SwaggerParameters {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.springsecurity.base.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo())
                .securitySchemes(Collections.singletonList(new ApiKey("Access Token", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiInfo metaInfo() {
        return new ApiInfo(
                TITLE,
                DESCRIPTION,
                API_VERSION,
                "Terms of Service",
                CONTACT,
                "MIT License",
                "https://opensource.org/licenses/MIT", new ArrayList<>()
        );
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("ROLE_ADMIN", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return Collections.singletonList(new SecurityReference("Access Token", authorizationScopes));
    }

}
