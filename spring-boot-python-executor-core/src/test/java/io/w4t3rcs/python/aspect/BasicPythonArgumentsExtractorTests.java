package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static io.w4t3rcs.python.constant.TestConstants.EMPTY_ARGUMENTS;

@ExtendWith(MockitoExtension.class)
class BasicPythonArgumentsExtractorTests {
    @InjectMocks
    private BasicPythonArgumentsExtractor basicPythonArgumentsExtractor;
    @Mock
    private PythonMethodExtractor methodExtractor;
    @Mock
    private JoinPoint joinPoint;

    @Test
    void testGetArguments() {
        Mockito.when(methodExtractor.getMethodParameters(joinPoint)).thenReturn(EMPTY_ARGUMENTS);

        Assertions.assertEquals(EMPTY_ARGUMENTS, basicPythonArgumentsExtractor.getArguments(joinPoint));
    }

    @Test
    void testGetArgumentsWithAdditionalArguments() {
        Map<String, Object> additionalArguments = Map.of("a", new Object());

        Mockito.when(methodExtractor.getMethodParameters(joinPoint)).thenReturn(EMPTY_ARGUMENTS);

        Assertions.assertEquals(additionalArguments, basicPythonArgumentsExtractor.getArguments(joinPoint, additionalArguments));
    }
}
