package io.w4t3rcs.python.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for Python execution.
 * These properties control how Python scripts are executed and where they are located.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python".</p>
 */
@ConfigurationProperties("spring.python")
public record PythonProperties(String path) {
}
