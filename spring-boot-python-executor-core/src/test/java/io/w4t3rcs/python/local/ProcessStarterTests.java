package io.w4t3rcs.python.local;

import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.w4t3rcs.python.constant.TestConstants.*;
import static io.w4t3rcs.python.properties.PythonExecutorProperties.LocalProperties;

@ExtendWith(MockitoExtension.class)
class ProcessStarterTests {
    private static final LocalProperties LOCAL_PROPERTIES = new LocalProperties("python", false);
    @InjectMocks
    private ProcessStarterImpl processStarter;
    @Mock
    private PythonExecutorProperties executorProperties;
    @Mock
    private PythonFileHandler pythonFileHandler;


    @ParameterizedTest
    @ValueSource(strings = {SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3})
    void testStart(String script) {
        Mockito.when(executorProperties.local()).thenReturn(LOCAL_PROPERTIES);
        Mockito.when(pythonFileHandler.isPythonFile(script)).thenReturn(false);

        Process process = processStarter.start(script);
        Assertions.assertEquals(0, process.exitValue());
    }
}
