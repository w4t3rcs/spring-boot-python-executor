package io.w4t3rcs.python.aspect;

import io.w4t3rcs.python.annotation.PythonParam;
import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;
import java.util.Map;

public interface MethodHandlerAspect {
    /**
     * Gets the Method object from a JoinPoint.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @return The Method object representing the intercepted method
     */
    Method getMethod(JoinPoint joinPoint);

    /**
     * Extracts method parameters from a join point and creates a map of parameter names to values.
     * If a parameter is annotated with the {@link PythonParam}, the name from the annotation is used.
     * Otherwise, the parameter's actual name is used.
     *
     * @param joinPoint The join point representing the intercepted method call
     * @return A map of parameter names to their values
     */
    Map<String, Object> getMethodParameters(JoinPoint joinPoint);
}
