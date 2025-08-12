package io.w4t3rcs.python.dto;

/**
 * Response wrapper for Python script execution result.
 *
 * @param <R> the type of the execution result body
 * @param body the result returned from executing the Python script
 */
public record PythonExecutionResponse<R>(R body) {
}
