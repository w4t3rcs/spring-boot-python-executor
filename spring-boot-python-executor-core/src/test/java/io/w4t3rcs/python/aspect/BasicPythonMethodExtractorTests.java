package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
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
class BasicPythonMethodExtractorTests {
    @InjectMocks
    private BasicPythonMethodExtractor basicPythonMethodExtractor;
    @Mock
    private JoinPoint joinPoint;
    @Mock
    private MethodSignature methodSignature;

    @Test
    void testGetMethod() {
        Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
        Mockito.when(methodSignature.getMethod()).thenReturn(DUMMY_METHOD);

        Assertions.assertEquals(DUMMY_METHOD, basicPythonMethodExtractor.getMethod(joinPoint));
    }

    @Test
    void testGetMethodParameters() {
        String aParamValue = "a";
        String bParamValue = "b";
        Map<String, String> result = Map.of(A_PYTHON_PARAM, aParamValue, CUSTOM_PYTHON_PARAM, bParamValue);

        Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
        Mockito.when(methodSignature.getMethod()).thenReturn(DUMMY_METHOD);
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{aParamValue, bParamValue});
        Mockito.when(methodSignature.getParameterNames()).thenReturn(new String[]{aParamValue, bParamValue});

        Assertions.assertEquals(result, basicPythonMethodExtractor.getMethodParameters(joinPoint));
    }
}
