package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.file.BasicPythonFileHandler;
import io.w4t3rcs.python.file.PythonFileHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for managing Python files.
 *
 * <p>These properties specify how Python files are located and maintained within the application.</p>
 *
 * <p>Properties are bound from the application configuration using the prefix
 * {@code spring.python.file}.</p>
 *
 * <p><b>Example (application.yml):</b>
 * <pre>{@code
 * spring:
 *   python:
 *     file:
 *       path: /python/
 * }</pre>
 * </p>
 *
 * @param path the base directory path where Python files are stored, must not be null or blank
 * @see PythonFileHandler
 * @see BasicPythonFileHandler
 * @author w4t3rcs
 * @since 1.0.0
 */
@ConfigurationProperties("spring.python.file")
public record PythonFileProperties(String path) {
}
