package io.w4t3rcs.python.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Python files.
 * These properties control how Python files are maintained.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.file".</p>
 */
@ConfigurationProperties("spring.python.file")
public record PythonFileProperties(String path, boolean cacheable) {
}
