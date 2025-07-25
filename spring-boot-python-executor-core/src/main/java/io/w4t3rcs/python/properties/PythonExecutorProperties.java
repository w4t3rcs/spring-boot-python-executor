package io.w4t3rcs.python.config;

import io.w4t3rcs.python.executor.PythonExecutor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuration properties for choosing needed {@link PythonExecutor} implementation.
 * These properties allow the {@link PythonExecutor} implementation to work properly.
 * 
 * <p>Properties are bound from the application configuration using the prefix "spring.python.executor".</p>
 */
@ConfigurationProperties("spring.python.executor")
public record PythonExecutorProperties(@DefaultValue("local") Type type, LocalProperties local, RestProperties rest, GrpcProperties grpc) {
    public enum Type {
        LOCAL, REST, GRPC
    }

    public record LocalProperties(@DefaultValue("python") String startCommand, @DefaultValue("true") boolean loggable) {
    }

    public record RestProperties(@DefaultValue("http://0.0.0.0") String host,
                                 @DefaultValue("8000") int port,
                                 String username,
                                 String password,
                                 @DefaultValue("${spring.python.executor.rest.host}:${spring.python.executor.rest.port}/script") String uri) {
    }

    public record GrpcProperties(@DefaultValue("http://0.0.0.0") String host,
                                 @DefaultValue("50051") int port,
                                 String username,
                                 String password,
                                 @DefaultValue("${spring.python.executor.grpc.host}:${spring.python.executor.grpc.port}") String uri) {
    }
}
