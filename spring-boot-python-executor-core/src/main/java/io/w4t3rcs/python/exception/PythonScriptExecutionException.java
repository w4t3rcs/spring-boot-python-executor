package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.executor.GrpcPythonExecutor;
import io.w4t3rcs.python.executor.LocalPythonExecutor;
import io.w4t3rcs.python.executor.RestPythonExecutor;

/**
 * Exception thrown when a Python script execution process is interrupted.
 * <p>
 * This typically occurs when a thread running the Python script is interrupted,
 * or when the execution process is forcibly terminated.
 * <p>
 * This exception extends {@link RuntimeException} and signals
 * an abnormal termination of the script execution flow.
 *
 * @see LocalPythonExecutor
 * @see RestPythonExecutor
 * @see GrpcPythonExecutor
 * @since 1.0.0
 * @author w4t3rcs
 */
public class PythonScriptExecutionException extends RuntimeException {
    /**
     * Constructs a new {@code PythonScriptExecutionException} with the specified cause.
     *
     * @param cause the underlying cause of the interruption (non-null)
     */
    public PythonScriptExecutionException(Throwable cause) {
        super(cause);
    }
}