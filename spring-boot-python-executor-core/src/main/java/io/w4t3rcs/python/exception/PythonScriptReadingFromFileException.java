package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.file.BasicPythonFileHandler;

/**
 * Exception thrown when there is an error reading a Python script from a file.
 * <p>
 * This typically occurs due to I/O errors, such as the file being inaccessible,
 * missing, corrupted, or due to insufficient permissions.
 * <p>
 * This exception extends {@link RuntimeException} and indicates failure
 * during the reading process of a Python script file.
 *
 * @see BasicPythonFileHandler
 * @since 1.0.0
 * @author w4t3rcs
 */
public class PythonScriptReadingFromFileException extends RuntimeException {
    /**
     * Constructs a new {@code PythonScriptReadingFromFileException} with the specified cause.
     *
     * @param cause the underlying cause of the failure to read the script file (non-null)
     */
    public PythonScriptReadingFromFileException(Throwable cause) {
        super(cause);
    }
}
