package io.w4t3rcs.python;

import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class PythonRestServerContainer extends PythonServerContainer<PythonRestServerContainer> {
    private static final DockerImageName DOCKER_IMAGE_NAME = DockerImageName.parse("w4t3rcs/spring-boot-python-executor-python-rest-server");
    private static final int SERVER_DEFAULT_PORT = 8000;

    public PythonRestServerContainer(String image) {
        this(DockerImageName.parse(image));
    }

    public PythonRestServerContainer(DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DOCKER_IMAGE_NAME);
        this.addExposedPort(SERVER_DEFAULT_PORT);
        this.waitingFor(
                new WaitAllStrategy()
                        .withStartupTimeout(Duration.ofMinutes(5))
                        .withStrategy(Wait.forListeningPort())
                        .withStrategy(Wait.forHttp("/script").forPort(SERVER_DEFAULT_PORT)));
    }

    public String getServiceUrl() {
        return "http://" + this.getHost() + ":" + this.getMappedPort(SERVER_DEFAULT_PORT) + "/script";
    }
}
