package io.w4t3rcs.python.exception;

/**
 * Exception thrown when there is an error creating a gatewayObject for Python integration.
 * This typically occurs when there are issues establishing communication between
 * Java and Python using the Py4J bridge.
 */
public class GatewayServerCreationException extends RuntimeException {
    public GatewayServerCreationException(Throwable cause) {
        super(cause);
    }
}
