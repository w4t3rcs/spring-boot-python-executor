package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.resolver.Py4JResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Py4J integration.
 * <p>
 * These properties define how the Py4J gateway is configured for communication between Java and Python processes.
 * The configuration is loaded from the application configuration files
 * (e.g. {@code application.yml} or {@code application.properties}) using the prefix {@code spring.python.py4j}.
 *
 * <p><b>Example (application.yml):</b>
 * <pre>{@code
 * spring:
 *   python:
 *     py4j:
 *       enabled: true
 *       host: localhost
 *       port: 25333
 *       pythonHost: localhost
 *       pythonPort: 25334
 *       connectTimeout: 5000
 *       readTimeout: 10000
 *       authToken: secret-token
 *       loggable: true
 * }</pre>
 *
 * @param enabled whether Py4J integration is enabled
 * @param host Java-side host name or IP address
 * @param port Java-side port
 * @param pythonHost Python-side host name or IP address
 * @param pythonPort Python-side port
 * @param connectTimeout connection timeout in milliseconds
 * @param readTimeout read timeout in milliseconds
 * @param authToken optional authentication token
 * @param loggable whether to log gateway communications
 * @see Py4JResolver
 * @author w4t3rcs
 * @since 1.0.0
 * @see <a href="https://www.py4j.org/">Py4J Documentation</a>
 */
@ConfigurationProperties("spring.python.py4j")
public record Py4JProperties(boolean enabled, String host, int port, String pythonHost, int pythonPort, int connectTimeout, int readTimeout, String authToken, boolean loggable) {
}
