package io.w4t3rcs.demo;

import io.w4t3rcs.demo.dto.MLScriptRequest;
import io.w4t3rcs.demo.service.PythonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
public class PythonServiceTests {
    @Autowired
    private PythonService pythonService;

    @Test
    public void doExecuteWithPython() {
        MLScriptRequest request = Mockito.mock(MLScriptRequest.class);
        pythonService.doSomethingWithPythonInside(request);
    }
}
