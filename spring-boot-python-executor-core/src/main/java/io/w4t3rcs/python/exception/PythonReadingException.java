package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.local.BasicPythonErrorProcessHandler;
import io.w4t3rcs.python.local.BasicPythonInputProcessHandler;

/**
 * Exception thrown when an error occurs while reading output from a Python process.
 * <p>
 * This exception typically indicates I/O errors encountered when reading
 * from the standard output or error streams of a Python process.
 * <p>
 * It extends {@link RuntimeException} and signals a failure in
 * communication or data retrieval from the Python execution environment.
 *
 * @see BasicPythonInputProcessHandler
 * @see BasicPythonErrorProcessHandler
 * @since 1.0.0
 * @author w4t3rcs
 */
public class PythonReadingException extends RuntimeException {
    /**
     * Constructs a new {@code PythonReadingException} with the specified cause.
     *
     * @param cause the underlying cause of the failure (non-null)
     */
    public PythonReadingException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code PythonReadingException} with the specified detail message.
     *
     * @param message the detail message explaining the cause (non-null)
     */
    public PythonReadingException(String message) {
        super(message);
    }
}
