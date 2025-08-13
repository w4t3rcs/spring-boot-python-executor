package io.w4t3rcs.demo;

import io.w4t3rcs.demo.service.SimplePythonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class PythonServiceTests {
    @Autowired
    private SimplePythonService simplePythonService;

    @Test
    public void doExecuteWithPython() {
        simplePythonService.doSomethingWithPythonBefore();
        simplePythonService.doSomethingWithPythonInside();
        simplePythonService.doSomethingWithPythonAfter();
    }
}
