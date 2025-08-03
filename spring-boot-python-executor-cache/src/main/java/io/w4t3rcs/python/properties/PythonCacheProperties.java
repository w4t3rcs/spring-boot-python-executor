package io.w4t3rcs.python.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.python.cache")
public record PythonCacheProperties(boolean enabled, String name, PythonCacheLevel[] levels, KeyProperties key, FileProperties file) {
    public enum PythonCacheLevel {
        FILE, RESOLVER, EXECUTOR, PROCESSOR
    }

    public record KeyProperties(String hashAlgorithm, String charset, String delimiter) {
    }

    public record FileProperties(String pathsName, String bodiesName) {
    }
}
