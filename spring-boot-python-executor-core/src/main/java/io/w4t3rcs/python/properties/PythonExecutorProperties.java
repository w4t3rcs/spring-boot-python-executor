package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.executor.GrpcPythonExecutor;
import io.w4t3rcs.python.executor.LocalPythonExecutor;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.executor.RestPythonExecutor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for selecting and configuring the {@link PythonExecutor} implementation.
 *
 * <p>These properties control which {@link PythonExecutor} type is used and provide necessary
 * configuration details for each supported executor type.</p>
 *
 * <p>Properties are bound from the application configuration using the prefix
 * {@code spring.python.executor}.</p>
 *
 * <p><b>Executor types:</b>
 * <ul>
 *   <li>{@link Type#LOCAL} — executes Python scripts locally using a process.</li>
 *   <li>{@link Type#REST} — executes Python scripts via a REST endpoint.</li>
 *   <li>{@link Type#GRPC} — executes Python scripts via a gRPC endpoint.</li>
 * </ul>
 * </p>
 *
 * <p><b>Example (application.yml):</b>
 * <pre>{@code
 * spring:
 *   python:
 *     executor:
 *       type: local
 *       local:
 *         start-command: python
 *         loggable: true
 * }</pre>
 * </p>
 *
 * @param type the {@link Type} of Python executor to use, must not be null
 * @param local configuration properties for the local executor, must not be null
 * @param rest configuration properties for the REST executor, must not be null
 * @param grpc configuration properties for the gRPC executor, must not be null
 * @see PythonExecutor
 * @see LocalPythonExecutor
 * @see RestPythonExecutor
 * @see GrpcPythonExecutor
 * @author w4t3rcs
 * @since 1.0.0
 */
@ConfigurationProperties("spring.python.executor")
public record PythonExecutorProperties(Type type, LocalProperties local, RestProperties rest, GrpcProperties grpc) {
    public enum Type {
        LOCAL, REST, GRPC
    }

    /**
     * Configuration properties for local Python executor.
     *
     * @param startCommand the command to start Python interpreter, must not be null or blank
     * @param loggable flag indicating if output should be logged
     */
    public record LocalProperties(String startCommand, boolean loggable) {
    }

    /**
     * Configuration properties for REST-based Python executor.
     *
     * @param host REST service host, must not be null or blank
     * @param port REST service port
     * @param token authentication token
     * @param uri full URI to REST endpoint, must not be null or blank
     */
    public record RestProperties(String host, int port, String token, String uri) {
    }

    /**
     * Configuration properties for gRPC-based Python executor.
     *
     * @param host gRPC service host, must not be null or blank
     * @param port gRPC service port
     * @param token authentication token
     * @param uri full URI to gRPC endpoint, must not be null or blank
     */
    public record GrpcProperties(String host, int port, String token, String uri) {
    }
}
