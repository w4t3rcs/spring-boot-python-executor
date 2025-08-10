package io.w4t3rcs.python.local;

import io.w4t3rcs.python.properties.PythonExecutorProperties;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.w4t3rcs.python.constant.TestConstants.SIMPLE_SCRIPT_3;
import static io.w4t3rcs.python.properties.PythonExecutorProperties.LocalProperties;
import static io.w4t3rcs.python.properties.PythonResolverProperties.ResultProperties;

@ExtendWith(MockitoExtension.class)
class BasicPythonInputProcessHandlerTests {
    private static final LocalProperties LOCAL_PROPERTIES = new LocalProperties(null, true);
    private static final ResultProperties RESULT_PROPERTIES = new ResultProperties(null, "r4java", 0, 0);
    @InjectMocks
    private BasicPythonInputProcessHandler inputProcessHandler;
    @Mock
    private PythonExecutorProperties executorProperties;
    @Mock
    private PythonResolverProperties resolverProperties;


    @SneakyThrows
    @Test
    void testHandle() {
        Process process = new ProcessBuilder("python", "-c", SIMPLE_SCRIPT_3).start();
        process.waitFor();
        Assumptions.assumeTrue(process.exitValue() == 0);

        Mockito.when(executorProperties.local()).thenReturn(LOCAL_PROPERTIES);
        Mockito.when(resolverProperties.result()).thenReturn(RESULT_PROPERTIES);

        String result = inputProcessHandler.handle(process);
        Assertions.assertEquals("4", result);
    }
}
