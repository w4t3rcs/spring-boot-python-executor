package io.w4t3rcs.python.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of {@link PythonArgumentsExtractor} that combines method parameters
 * extracted via a {@link PythonMethodExtractor} with additional external arguments.
 * <p>
 * This class uses the provided {@link PythonMethodExtractor} to extract method parameter
 * names and values from the given {@link JoinPoint}.
 * Then it merges these with the supplied additional arguments.
 * If the same argument name exists in both maps, the additional argument
 * value will override the method parameter value.
 * </p>
 * <p>
 * The returned map is mutable and contains all combined arguments.
 * </p>
 *
 * @see PythonArgumentsExtractor
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class BasicPythonArgumentsExtractor implements PythonArgumentsExtractor {
    private final PythonMethodExtractor methodExtractor;

    /**
     * Extracts method parameters from the {@link JoinPoint} and merges them with the given {@code additionalArguments}.
     * Additional arguments take precedence in case of key collisions.
     *
     * @param joinPoint the join point representing the method invocation, must not be {@code null}
     * @param additionalArguments additional arguments to include, must not be {@code null} (can be empty)
     * @return a combined map of argument names to values, never {@code null}
     */
    @Override
    public Map<String, Object> getArguments(JoinPoint joinPoint, Map<String, Object> additionalArguments) {
        Map<String, Object> arguments = new HashMap<>();
        Map<String, Object> methodArguments = methodExtractor.getMethodParameters(joinPoint);
        arguments.putAll(methodArguments);
        arguments.putAll(additionalArguments);
        return arguments;
    }
}
