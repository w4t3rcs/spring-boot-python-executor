package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.local.ProcessStarterImpl;

/**
 * Exception thrown when an error occurs while starting a process.
 * <p>
 * This exception typically indicates issues such as invalid commands,
 * insufficient permissions, or I/O errors that prevent the creation
 * of a new process.
 * <p>
 * It extends {@link RuntimeException} and signals a failure that
 * usually requires corrective action in configuration or environment.
 *
 * @see ProcessStarterImpl
 * @since 1.0.0
 * @author w4t3rcs
 */
public class ProcessStartException extends RuntimeException {
    /**
     * Constructs a new {@code ProcessStartException} with the specified cause.
     *
     * @param cause the underlying cause of the failure (non-null)
     */
    public ProcessStartException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code ProcessStartException} with the specified detail message.
     *
     * @param message the detail message explaining the cause (non-null)
     */
    public ProcessStartException(String message) {
        super(message);
    }
}
