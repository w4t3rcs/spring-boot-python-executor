package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Interface for extracting method-related information from an {@link JoinPoint}.
 * <p>
 * Implementations provide access to the invoked {@link Method} and its parameters
 * as a mapping from parameter names to their corresponding argument values.
 * </p>
 * <p>
 * This interface is typically used in aspect-oriented programming (AOP) contexts
 * where method metadata and runtime arguments need to be inspected or manipulated,
 * for example, to support script execution or logging.
 * </p>
 *
 * @see BasicPythonMethodExtractor
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface PythonMethodExtractor {
    /**
     * Retrieves the {@link Method} instance corresponding to the given {@link JoinPoint}.
     *
     * @param joinPoint the join point representing the method invocation, must not be {@code null}
     * @return the {@link Method} being invoked, never {@code null} for a valid join point
     */
    Method getMethod(JoinPoint joinPoint);

    /**
     * Extracts method parameter names and their corresponding argument values from the given {@link JoinPoint}.
     *
     * @param joinPoint the join point representing the method invocation; must not be {@code null}
     * @return a {@link Map} where keys are parameter names and values are argument objects, never {@code null}, but may be empty if the method has no parameters
     */
    Map<String, Object> getMethodParameters(JoinPoint joinPoint);
}
