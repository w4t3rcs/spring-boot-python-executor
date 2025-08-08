package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.aspect.MultiPythonScriptExtractor;
import io.w4t3rcs.python.aspect.PythonAnnotationValueExtractor;
import io.w4t3rcs.python.aspect.SinglePythonScriptExtractor;

/**
 * Runtime exception thrown when extraction of values from annotations fails.
 * <p>
 * This exception indicates that an error occurred during reflection or
 * processing of annotation values, such as missing methods or invocation issues.
 * <p>
 * Typically thrown by implementations of {@link PythonAnnotationValueExtractor}
 * when they cannot properly extract annotation values.
 * <p>
 * This exception is unchecked to allow propagation without mandatory handling,
 * signaling a programming or configuration error.
 *
 * @see SinglePythonScriptExtractor
 * @see MultiPythonScriptExtractor
 * @since 1.0.0
 * @author w4t3rcs
 */
public class AnnotationValueExtractingException extends RuntimeException {
    /**
     * Constructs a new {@code AnnotationValueExtractingException} with no detail message.
     */
    public AnnotationValueExtractingException() {
        super();
    }

    /**
     * Constructs a new {@code AnnotationValueExtractingException} with the specified cause.
     *
     * @param cause the cause of this exception (may be {@code null})
     */
    public AnnotationValueExtractingException(Throwable cause) {
        super(cause);
    }
}

