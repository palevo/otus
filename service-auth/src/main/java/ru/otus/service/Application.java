package ru.otus.service;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

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

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public WebClient webClient() {
        return WebClient.create("http://127.0.0.1:" + serverPort);
    }

    @Bean
    public OpenAPI openAPI(@Value("${otus.description}") String appDescription, @Value("${otus.api-version}") String appVersion) {
        return new OpenAPI().info(new Info()
                .title("OTUS.ru. Authentication service").description(appDescription).version(appVersion)
                .license(new License().name("Apache 2.0").url("http://arch.homework/otusapp")));
    }

    @Bean
    public GroupedOpenApi authGroupedOpenApi() {
        String[] paths = { "/auth/**" };
        return GroupedOpenApi.builder().group("auth").pathsToMatch(paths).build();
    }
}
