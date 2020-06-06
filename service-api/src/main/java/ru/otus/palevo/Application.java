package ru.otus.palevo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;

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
@SpringBootApplication
public class Application {

    /**
     * Application entry point
     *
     * @param args - parameters
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${application.description}") String appDescription, @Value("${application.version}") String appVersion) {
        return new OpenAPI().info(new Info()
                .title("OTUS.ru. OTUS application")
                .version(appVersion)
                .description(appDescription)
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0").url("http://arch.homework/otusapp")));
    }
}
