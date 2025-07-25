package io.w4t3rcs.python.exception;

/**
 * Exception thrown when there is an error processing Spring Expression Language (SpEL)
 * expressions in Python scripts. This typically occurs when there are issues evaluating
 * SpEL expressions or converting their results to JSON format.
 */
public class SpelythonProcessingException extends RuntimeException {
    public SpelythonProcessingException(Throwable cause) {
        super(cause);
    }
}
