package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;

import java.util.Map;

public interface PythonArgumentsExtractor {
    Map<String, Object> getArguments(JoinPoint joinPoint, Map<String, Object> additionalArguments);
}
