package com.example.mealserve.global.config;

import com.example.mealserve.global.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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

@OpenAPIDefinition(
        info = @Info(
                title = " 항해의 민족 API 명세서",
                description = "5조를 위한 API 명세서",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    @Profile("!Prod") // 운영 환경에는 Swagger를 비활성화하기 위해 추가
    public OpenAPI openAPI() {
        String jwtSchemeName = JwtUtil.AUTHORIZATION_HEADER;
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(BEARER_TOKEN_PREFIX)
                        .bearerFormat(JwtUtil.BEARER_PREFIX));

        // Swagger UI 접속 후, 딱 한 번만 accessToken을 입력해주면 모든 API에 토큰 인증 작업이 적용
        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)

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
                                new MediaType().schema(new Schema<>().$ref("#/components/schemas/LoginRequestDto"))))
                    )
                    .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse().description("로그인 성공"))
                        .addApiResponse("401", new ApiResponse().description("인증 실패"))
                    )));
    }

    // 로그인 컨트롤러
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
