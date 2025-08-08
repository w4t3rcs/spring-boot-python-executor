package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.file.BasicPythonFileHandler;

/**
 * Exception thrown when there is an error getting the path to a Python script.
 * <p>
 * This typically occurs due to issues such as invalid script paths,
 * missing files, or insufficient permissions when accessing the script location.
 * <p>
 * This exception extends {@link RuntimeException} and indicates failure
 * in resolving or retrieving the Python script path.
 *
 * @see BasicPythonFileHandler
 * @since 1.0.0
 * @author w4t3rcs
 */
public class PythonScriptPathGettingException extends RuntimeException {
    /**
     * Constructs a new {@code PythonScriptPathGettingException} with the specified cause.
     *
     * @param cause the underlying cause of the failure to get the script path (non-null)
     */
    public PythonScriptPathGettingException(Throwable cause) {
        super(cause);
    }
}
