package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonBefores;
import io.w4t3rcs.python.constant.TestConstants;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static io.w4t3rcs.python.constant.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class MultiPythonScriptExtractorTests {
    @InjectMocks
    private MultiPythonScriptExtractor multiPythonScriptExtractor;
    @Mock
    private PythonMethodExtractor methodExtractor;
    @Mock
    private JoinPoint joinPoint;

    @Test
    void testGetValue() {
        Map<String, String[]> annotationValue = Map.of(
                SIMPLE_SCRIPT_0, TEST_PROFILES,
                SIMPLE_SCRIPT_1, EMPTY_PROFILES,
                SIMPLE_SCRIPT_2, EMPTY_PROFILES
        );

        Mockito.when(methodExtractor.getMethod(joinPoint)).thenReturn(TestConstants.DUMMY_METHOD);

        Map<String, String[]> value = multiPythonScriptExtractor.getValue(joinPoint, PythonBefores.class);
        Assertions.assertEquals(annotationValue.keySet(), value.keySet());
        Assertions.assertEquals(annotationValue.get(SIMPLE_SCRIPT_0)[0], value.get(SIMPLE_SCRIPT_0)[0]);
    }
}
