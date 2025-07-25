package io.w4t3rcs.python.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import py4j.GatewayServer;

/**
 * Configuration properties for Py4J integration.
 * These properties control how the Py4J bridge between Java and Python is configured.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.py4j".</p>
 */
@ConfigurationProperties("spring.python.py4j")
public record Py4JProperties(@DefaultValue("false") boolean enabled,
                             @DefaultValue(GatewayServer.DEFAULT_ADDRESS) String host,
                             @DefaultValue("25333") int port,
                             @DefaultValue("25334") int pythonPort,
                             @DefaultValue("0") int connectTimeout,
                             @DefaultValue("0") int readTimeout,
                             @DefaultValue("true") boolean loggable) {
}
