package io.w4t3rcs.python.processor;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolverHolder;
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
class BasicPythonProcessorTests {
    @InjectMocks
    private BasicPythonProcessor pythonProcessor;
    @Mock
    private PythonFileHandler pythonFileHandler;
    @Mock
    private PythonExecutor pythonExecutor;
    @Mock
    private PythonResolverHolder pythonResolverHolder;

    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1
    })
    void testProcess(String script) {
        Mockito.when(pythonFileHandler.isPythonFile(script)).thenReturn(false);
        Mockito.when(pythonResolverHolder.resolveAll(script, EMPTY_ARGUMENTS)).thenReturn(script);
        Mockito.when((String) pythonExecutor.execute(script, OK_CLASS)).thenReturn(OK);

        String processed = pythonProcessor.process(script, OK_CLASS, EMPTY_ARGUMENTS);
        Assertions.assertEquals(OK, processed);
    }
}
