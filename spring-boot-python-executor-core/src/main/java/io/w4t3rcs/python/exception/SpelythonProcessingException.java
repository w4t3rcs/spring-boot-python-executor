package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.resolver.SpelythonResolver;

/**
 * Exception thrown when an error occurs during processing of Spring Expression Language (SpEL)
 * expressions in Python scripts.
 * <p>
 * This exception is typically raised when evaluation of SpEL expressions fails
 * or when there are issues converting the evaluation results to JSON format.
 * <p>
 * It extends {@link RuntimeException} and signals problems related to SpEL handling
 * within Python script integration.
 *
 * @see SpelythonResolver
 * @since 1.0.0
 * @author w4t3rcs
 */
public class SpelythonProcessingException extends RuntimeException {
    /**
     * Constructs a new {@code SpelythonProcessingException} with the specified cause.
     *
     * @param cause the underlying cause of the SpEL processing failure (non-null)
     */
    public SpelythonProcessingException(Throwable cause) {
        super(cause);
    }
}