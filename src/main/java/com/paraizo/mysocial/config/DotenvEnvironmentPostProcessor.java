package com.paraizo.mysocial.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;


import org.springframework.boot.EnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

// Loads KEY=VALUE pairs from a local .env file (if present) into the Environment, so
// application.properties can reference them via ${VAR_NAME}. Added last (lowest priority)
// so real OS/system environment variables - e.g. the ones docker-compose.yml injects into
// the app container - always win over .env, matching conventional dotenv tooling behavior.
// Registered via META-INF/spring.factories.

// This class is like the 'Impl' version of EnvironmentPostProcessor
// This is why META-INF/spring.factories file exist:
// it's Spring's way of asking "who out there implements this interface?"
// Implementing this method allow us to control Spring's startup sequence to do loadEnv()
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Path envFile = Path.of(".env");
        if (!Files.isReadable(envFile)) {
            return;
        }

        Map<String, Object> values = new LinkedHashMap<>();
        try {
            for (String line : Files.readAllLines(envFile)) {
                String trimmed = line.strip();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int separator = trimmed.indexOf('=');
                if (separator < 0) {
                    continue;
                }
                String key = trimmed.substring(0, separator).strip();
                String value = trimmed.substring(separator + 1).strip();
                values.put(key, value);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read .env file", e);
        }

        environment.getPropertySources().addLast(new MapPropertySource("dotenv", values));
    }
}
