package io.w4t3rcs.python.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.util.Map;

@RequiredArgsConstructor
public class BasicPythonArgumentsExtractor implements PythonArgumentsExtractor {
    private final MethodExtractor methodExtractor;

    @Override
    public Map<String, Object> getArguments(JoinPoint joinPoint, Map<String, Object> additionalArguments) {
        Map<String, Object> arguments = methodExtractor.getMethodParameters(joinPoint);
        arguments.putAll(additionalArguments);
        return arguments;
    }
}
