package io.w4t3rcs.python;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;

public class PythonGrpcServerContainerTests {
    @Rule
    public PythonGrpcServerContainer pythonGrpcServer = new PythonGrpcServerContainer("w4t3rcs/spring-boot-python-executor-python-grpc-server")
            .withAdditionalImports(new String[]{"numpy"});

    @Test
    public void testContainer() {
        pythonGrpcServer.start();
        Assertions.assertThat(pythonGrpcServer.isCreated()).isTrue();
        Assertions.assertThat(pythonGrpcServer.isRunning()).isTrue();
        pythonGrpcServer.stop();
    }
}
