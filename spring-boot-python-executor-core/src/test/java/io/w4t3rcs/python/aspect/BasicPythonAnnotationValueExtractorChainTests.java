package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonBefore;
import io.w4t3rcs.python.exception.AnnotationValueExtractingException;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static io.w4t3rcs.python.constant.TestConstants.SIMPLE_SCRIPT_0;
import static io.w4t3rcs.python.constant.TestConstants.TEST_PROFILES;

@ExtendWith(MockitoExtension.class)
class BasicPythonAnnotationValueExtractorChainTests {
    @InjectMocks
    private BasicPythonAnnotationValueExtractorChain basicPythonAnnotationValueExtractorChain;
    @Mock
    private PythonAnnotationValueExtractor annotationValueExtractor;
    @Mock
    private JoinPoint joinPoint;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(basicPythonAnnotationValueExtractorChain, "annotationValueExtractors", List.of(annotationValueExtractor));
    }

    @Test
    void testGetValue() {
        Map<String, String[]> annotationValue = Map.of(SIMPLE_SCRIPT_0, TEST_PROFILES);

        Mockito.when(annotationValueExtractor.getValue(joinPoint, PythonBefore.class)).thenReturn(annotationValue);

        Assertions.assertEquals(annotationValue, basicPythonAnnotationValueExtractorChain.getValue(joinPoint, PythonBefore.class));
    }

    @Test
    void testGetValueWithException() {
        Assertions.assertThrows(AnnotationValueExtractingException.class, () -> basicPythonAnnotationValueExtractorChain.getValue(joinPoint, PythonBefore.class));
    }
}
