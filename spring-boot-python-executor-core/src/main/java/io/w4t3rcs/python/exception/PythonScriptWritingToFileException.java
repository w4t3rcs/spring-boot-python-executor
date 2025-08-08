package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.file.BasicPythonFileHandler;

/**
 * Exception thrown when there is an error writing a Python script to a file.
 * <p>
 * This typically occurs due to I/O errors, such as insufficient disk space,
 * file permission issues, or the file being locked or inaccessible.
 * <p>
 * This exception extends {@link RuntimeException} and indicates failure
 * during the writing process of a Python script file.
 *
 * @see BasicPythonFileHandler
 * @since 1.0.0
 * @author w4t3rcs
 */
public class PythonScriptWritingToFileException extends RuntimeException {
    /**
     * Constructs a new {@code PythonScriptWritingToFileException} with the specified cause.
     *
     * @param cause the underlying cause of the failure to write the script file (non-null)
     */
    public PythonScriptWritingToFileException(Throwable cause) {
        super(cause);
    }
}
