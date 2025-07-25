package io.w4t3rcs.python.resolver;

import java.util.Map;

/**
 * Interface defining the contract for resolving Python scripts.
 * Implementations of this interface process Python scripts before execution,
 * applying transformations or resolving expressions within the script.
 */
public interface PythonResolver {
    /**
     * Processes a Python script, applying transformations or resolving expressions.
     *
     * @param script The Python script content
     * @param arguments A map of variables that may be used during resolution
     * @return The processed script after applying transformations
     */
    String resolve(String script, Map<String, Object> arguments);
}
