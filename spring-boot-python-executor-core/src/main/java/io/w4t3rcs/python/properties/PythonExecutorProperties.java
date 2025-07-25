package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.executor.PythonExecutor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for choosing needed {@link PythonExecutor} implementation.
 * These properties allow the {@link PythonExecutor} implementation to work properly.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.executor".</p>
 */
@ConfigurationProperties("spring.python.executor")
public record PythonExecutorProperties(Type type, LocalProperties local, RestProperties rest, GrpcProperties grpc) {
    public enum Type {
        LOCAL, REST, GRPC
    }

    public record LocalProperties(String startCommand, boolean loggable) {
    }

    public record RestProperties(String host, int port, String username, String password, String uri) {
    }

    public record GrpcProperties(String host, int port, String username, String password, String uri) {
    }
}
