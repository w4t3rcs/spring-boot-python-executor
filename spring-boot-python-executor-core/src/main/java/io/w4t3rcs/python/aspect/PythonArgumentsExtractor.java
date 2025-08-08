package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;

import java.util.Map;

/**
 * Interface for extracting method arguments as a map of names to values from a {@link JoinPoint}.
 * <p>
 * Implementations provide the logic to extract argument names and values, optionally
 * augmented by additional arguments supplied externally.
 * </p>
 * <p>
 * The default method {@link #getArguments(JoinPoint)} delegates to
 * {@link #getArguments(JoinPoint, Map)} with an empty additional arguments map.
 * </p>
 *
 * @see BasicPythonArgumentsExtractor
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface PythonArgumentsExtractor {
    /**
     * Extracts method arguments from the given {@link JoinPoint} without additional arguments.
     *
     * @param joinPoint the join point representing the method invocation, must not be {@code null}
     * @return a map of argument names to their corresponding values, never {@code null}
     */
    default Map<String, Object> getArguments(JoinPoint joinPoint) {
        return this.getArguments(joinPoint, Map.of());
    }

    /**
     * Extracts method arguments from the given {@link JoinPoint} and merges them with the supplied
     * additional arguments.
     *
     * @param joinPoint the join point representing the method invocation, must not be {@code null}
     * @param additionalArguments additional arguments to include in the returned map, must not be {@code null}
     * @return a map of argument names to their corresponding values including additional arguments, never {@code null}
     */
    Map<String, Object> getArguments(JoinPoint joinPoint, Map<String, Object> additionalArguments);
}
