package ru.otus.service;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Application starter class
 *
 * @author A.Osipov
 * @since 19.04.2020
 */
@EnableWebFlux
@EntityScan(basePackages = "ru.otus.common.domain")
@EnableJpaRepositories(basePackages = "ru.otus.common.repository")
@SpringBootApplication(scanBasePackages = "ru.otus")
public class Application {

    /**
     * Application entry point
     *
     * @param args - parameters
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value("${otus.url:}")
    private String baseUrl;

    @Bean
    public OpenAPI openAPI(@Value("${otus.description}") String appDescription, @Value("${otus.api-version}") String appVersion) {
        OpenAPI openapi = new OpenAPI();
        if (isNotBlank(baseUrl)) {
            Server server = new Server();
            server.setUrl(baseUrl);
            openapi.addServersItem(server);
        }
        return openapi.info(new Info()
                .title("OTUS.ru. API service").description(appDescription).version(appVersion)
                .license(new License().name("Apache 2.0").url("http://arch.homework/otusapp")));
    }

    @Bean
    public GroupedOpenApi authGroupedOpenApi() {
        String[] paths = { "/users/**" };
        return GroupedOpenApi.builder().group("users").pathsToMatch(paths).build();
    }
}
