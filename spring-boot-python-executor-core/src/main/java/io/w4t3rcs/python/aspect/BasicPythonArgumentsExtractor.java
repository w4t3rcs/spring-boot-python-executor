package io.w4t3rcs.python.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BasicPythonArgumentsExtractor implements PythonArgumentsExtractor {
    private final PythonMethodExtractor methodExtractor;

    @Override
    public Map<String, Object> getArguments(JoinPoint joinPoint, Map<String, Object> additionalArguments) {
        Map<String, Object> arguments = new HashMap<>();
        Map<String, Object> methodArguments = methodExtractor.getMethodParameters(joinPoint);
        arguments.putAll(methodArguments);
        arguments.putAll(additionalArguments);
        return arguments;
    }
}
