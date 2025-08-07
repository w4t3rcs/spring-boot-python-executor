package io.w4t3rcs.python.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.python.cache")
public record PythonCacheProperties(boolean enabled, PythonCacheLevel[] levels, NameProperties name, KeyProperties key) {
    public enum PythonCacheLevel {
        FILE, RESOLVER, EXECUTOR, PROCESSOR
    }

    public record NameProperties(String filePaths, String fileBodies, String resolver, String executor, String processor) {
    }

    public record KeyProperties(String hashAlgorithm, String charset, String delimiter) {
    }
}
