package io.w4t3rcs.python.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.python.aspect")
public record PythonAspectProperties(AsyncScope[] asyncScopes) {
    public enum AsyncScope {
        BEFORE, AFTER
    }
}
