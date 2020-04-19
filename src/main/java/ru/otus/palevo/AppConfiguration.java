package ru.otus.palevo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Service configuration
 *
 * @author A.Osipov
 * @since 19.04.2020
 */
@Slf4j
@Getter
@Setter
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = AppConfiguration.PREFIX)
class AppConfiguration {

    public static final String PREFIX = "otus";
}
