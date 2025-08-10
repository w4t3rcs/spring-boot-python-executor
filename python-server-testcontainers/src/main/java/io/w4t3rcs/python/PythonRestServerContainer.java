package io.w4t3rcs.python;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

/**
 * Testcontainers-based container for running a Python REST server used by the
 * {@code spring-boot-python-executor} project.
 *
 * <p>This container provides an HTTP interface for executing Python scripts in a
 * controlled environment. It exposes a single REST endpoint at the
 * {@code /script} path on the configured port. By default, the container runs
 * on port {@value #SERVER_DEFAULT_PORT} and uses the official Docker image
 * {@code w4t3rcs/spring-boot-python-executor-python-rest-server}.</p>
 *
 * <h3>Example usage</h3>
 * <pre>{@code
 * try (PythonRestServerContainer restContainer = new PythonRestServerContainer("w4t3rcs/spring-boot-python-executor-python-rest-server:latest")
 *         .withUsername("user")
 *         .withPassword("pass")) {
 *     restContainer.start();
 *
 *     //Do something after the container has been started
 * }
 * }</pre>
 *
 * @see PythonServerContainer
 * @see PythonGrpcServerContainer
 * @author w4t3rcs
 * @since 1.0.0
 */
public class PythonRestServerContainer extends PythonServerContainer<PythonRestServerContainer> {
    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("w4t3rcs/spring-boot-python-executor-python-rest-server");
    private static final int SERVER_DEFAULT_PORT = 8000;

    /**
     * Creates a new {@link PythonRestServerContainer} instance using a Docker image name string.
     *
     * @param image non-null string representation of the Docker image to use, must be compatible with {@code w4t3rcs/spring-boot-python-executor-python-rest-server}
     */
    public PythonRestServerContainer(String image) {
        this(DockerImageName.parse(image));
    }

    /**
     * Creates a new {@link PythonRestServerContainer} instance using a {@link DockerImageName}.
     *
     * <p>The image name is validated for compatibility with {@code w4t3rcs/spring-boot-python-executor-python-rest-server} before the
     * container is configured.</p>
     *
     * @param dockerImageName non-null {@link DockerImageName} instance
     */
    public PythonRestServerContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DOCKER_IMAGE_NAME);
        this.addExposedPort(SERVER_DEFAULT_PORT);
        this.waitingFor(
                new WaitAllStrategy()
                        .withStartupTimeout(Duration.ofMinutes(5))
                        .withStrategy(Wait.forListeningPort()));
    }

    /**
     * Returns the base URL of the Python REST server endpoint.
     *
     * <p>The returned URL is always in the form:
     * {@code http://<host>:<mappedPort>/script}</p>
     *
     * @return non-null base URL string of the REST endpoint
     */
    public String getServerUrl() {
        return "http://" + this.getHost() + ":" + this.getMappedPort(SERVER_DEFAULT_PORT) + "/script";
    }
}