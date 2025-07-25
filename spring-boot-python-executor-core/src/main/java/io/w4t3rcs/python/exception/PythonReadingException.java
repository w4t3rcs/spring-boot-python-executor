package io.w4t3rcs.python.exception;

/**
 * Exception thrown when there is an error reading output from a Python process.
 * This typically occurs when there are I/O errors while reading from the
 * standard output or error streams of a Python process.
 */
public class PythonReadingException extends RuntimeException {
    public PythonReadingException(Throwable cause) {
        super(cause);
    }
}
