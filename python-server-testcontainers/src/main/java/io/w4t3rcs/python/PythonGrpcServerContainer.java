package io.w4t3rcs.python;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class PythonGrpcServerContainer extends PythonServerContainer<PythonGrpcServerContainer> {
    public static final String PYTHON_SERVER_THREAD_POOL_MAX_WORKERS_ENV = "PYTHON_SERVER_THREAD_POOL_MAX_WORKERS";
    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("w4t3rcs/spring-boot-python-executor-python-grpc-server");
    private static final int SERVER_DEFAULT_PORT = 50051;
    public static final String GRPC_SERVER_RUNNING_MESSAGE = ".*gRPC server running.+";

    public PythonGrpcServerContainer(String image) {
        this(DockerImageName.parse(image));
    }

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

    public PythonGrpcServerContainer withThreadPoolMaxWorkers(int maxWorkers) {
        this.withEnv(PYTHON_SERVER_THREAD_POOL_MAX_WORKERS_ENV, String.valueOf(maxWorkers));
        return this.self();
    }

    public String getServerUrl() {
        return this.getHost() + ":" + this.getMappedPort(SERVER_DEFAULT_PORT);
    }
}
