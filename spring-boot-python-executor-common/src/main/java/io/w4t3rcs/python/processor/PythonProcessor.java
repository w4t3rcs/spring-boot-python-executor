package io.w4t3rcs.python.processor;

import io.w4t3rcs.python.dto.PythonExecutionResponse;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.resolver.PythonResolverHolder;

import java.util.Map;

/**
 * Defines the contract for processing and executing Python scripts, acting as a bridge between
 * {@link PythonExecutor} and {@link PythonResolverHolder}.
 *
 * <p>Implementations of this interface are responsible for:</p>
 * <ul>
 *     <li>Pre-processing Python scripts before execution</li>
 *     <li>Applying transformations and expression resolution using registered resolvers using {@link PythonResolver} instances</li>
 *     <li>Delegating the execution to a {@link PythonExecutor}</li>
 * </ul>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * PythonProcessor processor = ...;
 * String script = "print('Hello World ')";
 * processor.process(script);
 * }</pre>
 * @see PythonExecutor
 * @see PythonResolverHolder
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface PythonProcessor {
    /**
     * Processes and executes a Python script without additional arguments or body mapping.
     *
     * @param script non-{@code null} Python script to execute
     */
    default PythonExecutionResponse<?> process(String script) {
        return this.process(script, null, Map.of());
    }

    /**
     * Processes and executes a Python script with the given argument map.
     *
     * @param script non-{@code null} Python script to execute
     * @param arguments a map of arguments accessible to resolvers during preprocessing
     */
    default PythonExecutionResponse<?> process(String script, Map<String, Object> arguments) {
        return this.process(script, null, arguments);
    }

    /**
     * Processes and executes a Python script, mapping the body to the specified type.
     *
     * @param <R> the type of body expected from script execution
     * @param script non-{@code null} Python script to execute
     * @param resultClass the class representing the expected body type (nullable)
     * @return the body of execution cast to {@code R}, or {@code null} if the script returns nothing
     */
    default <R> PythonExecutionResponse<R> process(String script, Class<? extends R> resultClass) {
        return this.process(script, resultClass, Map.of());
    }

    /**
     * Processes and executes a Python script with arguments and optional body mapping.
     *
     * @param <R> the type of body expected from script execution
     * @param script non-{@code null} Python script to execute
     * @param resultClass the class representing the expected body type (nullable)
     * @param arguments a map of arguments accessible to resolvers during preprocessing
     * @return the body of execution cast to {@code R}, or {@code null} if the script returns nothing
     */
    <R> PythonExecutionResponse<R> process(String script, Class<? extends R> resultClass, Map<String, Object> arguments);
}