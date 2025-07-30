package io.w4t3rcs.python.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.local.ProcessFinisher;
import io.w4t3rcs.python.local.ProcessHandler;
import io.w4t3rcs.python.local.ProcessStarter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.w4t3rcs.python.constant.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class LocalPythonExecutorTests {
    @InjectMocks
    private LocalPythonExecutor localPythonExecutor;
    @Mock
    private ProcessStarter processStarter;
    @Mock
    private ProcessHandler<String> inputProcessHandler;
    @Mock
    private ProcessHandler<Void> errorProcessHandler;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ProcessFinisher processFinisher;

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3})
    void testExecute(String script) {
        Process process = new ProcessBuilder("python", "-c", script).start();

        Mockito.when(processStarter.start(script)).thenReturn(process);
        Mockito.when(inputProcessHandler.handle(process)).thenReturn(OK);
        Mockito.doNothing().when(processFinisher).finish(process);
        Mockito.when((String) objectMapper.readValue(OK, OK_CLASS)).thenReturn(OK);

        String executed = localPythonExecutor.execute(script, OK_CLASS);
        Assertions.assertEquals(OK, executed);
    }
}
