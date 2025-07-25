package io.w4t3rcs.python.exception;

/**
 * Exception thrown when there is an error starting a process.
 * This typically occurs when there are issues creating a new process,
 * such as invalid commands, insufficient permissions, or I/O errors.
 */
public class ProcessStartException extends RuntimeException {
    public ProcessStartException(Throwable cause) {
        super(cause);
    }
}
