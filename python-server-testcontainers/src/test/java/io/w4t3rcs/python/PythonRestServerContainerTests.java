package io.w4t3rcs.python;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;

public class PythonRestServerContainerTests {
    @Rule
    public PythonRestServerContainer pythonRestServer = new PythonRestServerContainer("w4t3rcs/spring-boot-python-executor-python-grpc-server")
            .withAdditionalImports(new String[]{"numpy"});

    @Test
    public void testContainer() {
        pythonRestServer.start();
        Assertions.assertThat(pythonRestServer.isCreated()).isTrue();
        Assertions.assertThat(pythonRestServer.isRunning()).isTrue();
        pythonRestServer.stop();
    }
}
