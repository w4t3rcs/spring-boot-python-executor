package io.w4t3rcs.python.util;

import io.w4t3rcs.python.annotation.PythonBefore;
import io.w4t3rcs.python.annotation.PythonParam;
import io.w4t3rcs.python.constant.TestConstants;
import io.w4t3rcs.python.processor.PythonProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class AspectUtilTests {
    @Mock
    private JoinPoint joinPoint;
    @Mock
    private MethodSignature methodSignature;
    @Mock
    private PythonProcessor pythonProcessor;
    private Method method;

    @BeforeEach
    void init() throws NoSuchMethodException {
        method = this.getClass().getDeclaredMethod("doDummy", String.class, String.class);
    }

    @Test
    void testHandlePythonAnnotation() {
        Mockito.when(pythonProcessor.process(TestConstants.SIMPLE_SCRIPT_3, null, null)).thenReturn(null);

        AspectUtil.handlePythonAnnotation(joinPoint, pythonProcessor, () -> TestConstants.SIMPLE_SCRIPT_3, point -> null);
        Mockito.verify(pythonProcessor, Mockito.times(1)).process(TestConstants.SIMPLE_SCRIPT_3, null, null);
    }

    @Test
    void testGetMethod() {
        Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
        Mockito.when(methodSignature.getMethod()).thenReturn(method);

        Method result = AspectUtil.getMethod(joinPoint);
        Assertions.assertEquals(method, result);
    }

    @Test
    void testGetPythonMethodParameters() {
        Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
        Mockito.when(methodSignature.getMethod()).thenReturn(method);
        Mockito.when(methodSignature.getParameterNames()).thenReturn(new String[]{"a", "b"});
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{"hello", "world"});

        Map<String, Object> parameters = AspectUtil.getPythonMethodParameters(joinPoint);
        Assertions.assertEquals(2, parameters.size());
        Assertions.assertTrue(parameters.containsKey("a"));
        Assertions.assertEquals("hello", parameters.get("a"));
        Assertions.assertTrue(parameters.containsKey("test_var"));
        Assertions.assertEquals("world", parameters.get("test_var"));
    }

    @PythonBefore("print(2 + 2)")
    private void doDummy(String a, @PythonParam("test_var") String b) {
        //Just a stub method that does nothing
    }
}
