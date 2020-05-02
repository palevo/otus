package ru.otus.palevo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Service configuration
 *
 * @author A.Osipov
 * @since 19.04.2020
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = AppConfiguration.PREFIX)
public class AppConfiguration {

    static final String PREFIX = "otus";

    private String version;
}
