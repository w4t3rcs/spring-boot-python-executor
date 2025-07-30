package io.w4t3rcs.python.processor;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.resolver.PythonResolver;

import java.util.Map;

/**
 * Interface for Python connecting {@link PythonExecutor} and {@link PythonResolver}.
 * Implementations of this interface process Python scripts before execution,
 * applying transformations or resolving expressions within the script and, also, script execution itself.
 */
public interface PythonProcessor {
    /**
     * Executes a Python script after processing it through a series of resolvers.
     *
     * @param <R> The type of result expected from the script execution
     * @param script The Python script to execute
     * @param resultClass The class representing the expected result type
     * @param arguments A map of arguments to be used by resolvers
     * @return The result of the script execution, cast to the specified result class
     */
    <R> R process(String script, Class<? extends R> resultClass, Map<String, Object> arguments);
}
