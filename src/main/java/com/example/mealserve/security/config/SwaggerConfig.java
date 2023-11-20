package com.example.mealserve.security.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "JWTAuth";

    @Bean
    @Profile("!Prod") // 운영 환경에서 Swagger 비활성화
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))

                .schema("AccountLoginRequestDto", new Schema<>()
                        .type("object")
                        .addProperty("email", new StringSchema().example("user@example.com"))
                        .addProperty("password", new StringSchema().example("password123!"))
                )

                // 로그인 수동경로 지정
                .path("/api/auth/login", new PathItem()
                        .post(new Operation()
                                .summary("로그인")
                                .description("사용자 로그인을 위한 엔드포인트")
                                .tags(Collections.singletonList("auth"))
                                .operationId("loginUser")
                                .requestBody(new RequestBody()
                                        .content(new Content()
                                                .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/AccountLoginRequestDto"))))
                                )
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("로그인 성공"))
                                        .addApiResponse("401", new ApiResponse().description("인증 실패"))
                                )));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("custom-auth")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi menuApi() {
        return GroupedOpenApi.builder()
                .group("menu-orders")
                .pathsToMatch("/api/stores/**")
                .build();
    }
}