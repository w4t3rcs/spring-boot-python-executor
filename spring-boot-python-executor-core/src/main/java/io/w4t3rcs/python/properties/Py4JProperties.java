package io.w4t3rcs.python.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Py4J integration.
 * These properties control how the Py4J bridge between Java and Python is configured.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.py4j".</p>
 */
@ConfigurationProperties("spring.python.py4j")
public record Py4JProperties(boolean enabled, String host, int port, String pythonHost, int pythonPort, int connectTimeout, int readTimeout, String authToken, boolean loggable) {
}
