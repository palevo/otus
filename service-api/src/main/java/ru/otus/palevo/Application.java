package ru.otus.palevo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.web.reactive.config.EnableWebFlux;

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
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }
}
