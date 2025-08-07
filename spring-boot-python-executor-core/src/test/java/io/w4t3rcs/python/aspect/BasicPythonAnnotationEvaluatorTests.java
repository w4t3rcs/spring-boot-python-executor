package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.processor.PythonProcessor;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static io.w4t3rcs.python.constant.TestConstants.SIMPLE_SCRIPT_0;
import static io.w4t3rcs.python.constant.TestConstants.TEST_PROFILES;

@ExtendWith(MockitoExtension.class)
class BasicPythonAnnotationEvaluatorTests {
    @InjectMocks
    private BasicPythonAnnotationEvaluator basicPythonAnnotationEvaluator;
    @Mock
    private ProfileChecker profileChecker;
    @Mock
    private PythonAnnotationValueExtractorChain annotationValueExtractorChain;
    @Mock
    private PythonArgumentsExtractor argumentsExtractor;
    @Mock
    private PythonProcessor pythonProcessor;
    @Mock
    private JoinPoint joinPoint;

    @Test
    void testEvaluate() {
        Map<String, String[]> annotationValue = Map.of(SIMPLE_SCRIPT_0, TEST_PROFILES);

        Mockito.when(annotationValueExtractorChain.getValue(joinPoint, null)).thenReturn(annotationValue);

        Assertions.assertDoesNotThrow(() -> basicPythonAnnotationEvaluator.evaluate(joinPoint, null));
    }
}
