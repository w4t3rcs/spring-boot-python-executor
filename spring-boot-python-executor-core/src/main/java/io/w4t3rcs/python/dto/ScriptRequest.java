package io.w4t3rcs.python.dto;

import io.w4t3rcs.python.executor.RestPythonExecutor;

/**
 * Data Transfer Object representing a request to execute a Python script
 * via the {@code python-rest-server}'s {@link RestPythonExecutor}.
 * <p>
 * This record encapsulates the Python script content to be executed.
 * It is immutable and thread-safe by design.
 * <p>
 * Example usage:
 * <pre>{@code
 * ScriptRequest request = new ScriptRequest("print('Hello, World!')");
 * }</pre>
 *
 * @see RestPythonExecutor
 * @since 1.0.0
 * @author w4t3rcs
 */
public record ScriptRequest(String script) {
}
