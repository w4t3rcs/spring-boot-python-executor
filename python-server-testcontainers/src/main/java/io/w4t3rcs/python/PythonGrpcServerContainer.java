package io.w4t3rcs.python;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * Container for running a Python gRPC server inside a Docker container.
 * <p>
 * Extends {@link PythonServerContainer} to provide configuration specific
 * to the Python gRPC server implementation used in the Docker image
 * {@code w4t3rcs/spring-boot-python-executor-python-grpc-server}.
 * <p>
 * The container exposes the standard gRPC port {@code 50051} and waits
 * for both the port availability and a specific log message indicating
 * successful startup.
 *
 * <pre>{@code
 * try (PythonGrpcServerContainer grpcServerContainer = new PythonGrpcServerContainer("w4t3rcs/spring-boot-python-executor-python-grpc-server:latest")
 *         .withUsername("user")
 *         .withPassword("pass")) {
 *     grpcServerContainer.start();
 *
 *     //Do something after the container has been started
 * }
 * }</pre>
 *
 * @see PythonServerContainer
 * @see PythonRestServerContainer
 * @author w4t3rcs
 * @since 1.0.0
 */
public class PythonGrpcServerContainer extends PythonServerContainer<PythonGrpcServerContainer> {
    public static final String PYTHON_SERVER_THREAD_POOL_MAX_WORKERS_ENV = "PYTHON_SERVER_THREAD_POOL_MAX_WORKERS";
    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("w4t3rcs/spring-boot-python-executor-python-grpc-server");
    private static final int SERVER_DEFAULT_PORT = 50051;
    private static final String GRPC_SERVER_RUNNING_MESSAGE = ".*gRPC server running.+";

    /**
     * Creates a new {@link PythonGrpcServerContainer} instance with the given Docker image name.
     * <p>
     * The provided image name must be compatible with
     * {@code w4t3rcs/spring-boot-python-executor-python-grpc-server}.
     *
     * @param image non-null Docker image name string, must be compatible with the expected base image
     */
    public PythonGrpcServerContainer(String image) {
        this(DockerImageName.parse(image));
    }

    /**
     * Creates a new {@link PythonGrpcServerContainer} instance with the given {@link DockerImageName}.
     * <p>
     * The provided Docker image must be compatible with
     * {@code w4t3rcs/spring-boot-python-executor-python-grpc-server}.
     * <p>
     * The container exposes port {@value #SERVER_DEFAULT_PORT} and waits
     * for the port to be listening as well as the log message defined by
     * {@value #GRPC_SERVER_RUNNING_MESSAGE} before considering itself started.
     * The startup timeout is 5 minutes.
     *
     * @param dockerImageName non-null {@link DockerImageName} instance, must be compatible with the expected base image
     */
    public PythonGrpcServerContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DOCKER_IMAGE_NAME);
        this.addExposedPort(SERVER_DEFAULT_PORT);
        this.waitingFor(
                new WaitAllStrategy()
                        .withStartupTimeout(Duration.ofMinutes(5))
                        .withStrategy(Wait.forListeningPort())
                        .withStrategy(Wait.forLogMessage(GRPC_SERVER_RUNNING_MESSAGE, 1)));
    }

    /**
     * Sets the maximum number of workers for the thread pool in the Python gRPC server.
     * <p>
     * This value is passed as an environment variable
     * {@value #PYTHON_SERVER_THREAD_POOL_MAX_WORKERS_ENV} to the container.
     *
     * @param maxWorkers non-negative integer specifying maximum thread pool workers, zero or negative values are accepted but their effect depends on server implementation
     * @return this container instance for fluent chaining, never {@code null}
     */
    public PythonGrpcServerContainer withThreadPoolMaxWorkers(int maxWorkers) {
        this.withEnv(PYTHON_SERVER_THREAD_POOL_MAX_WORKERS_ENV, String.valueOf(maxWorkers));
        return this.self();
    }

    /**
     * Returns the server URL in the form {@code host:port} where {@code host}
     * is the container host and {@code port} is the mapped port {@value #SERVER_DEFAULT_PORT}.
     * <p>
     * This method should only be called after the container has been started.
     *
     * @return non-null, non-empty string representing the server URL, e.g. {@code localhost:50051}
     */
    public String getServerUrl() {
        return this.getHost() + ":" + this.getMappedPort(SERVER_DEFAULT_PORT);
    }
}