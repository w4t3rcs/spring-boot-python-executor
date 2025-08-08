package io.w4t3rcs.python.exception;

import py4j.GatewayServer;

/**
 * Exception thrown when an error occurs during the creation of a gateway object
 * for Python integration.
 * <p>
 * This exception typically indicates issues in establishing communication between
 * Java and Python through the Py4J bridge, such as failure to start the gateway server
 * or internal connection problems.
 * <p>
 * Being a runtime exception, it signals a configuration or environment issue that
 * should be resolved to enable proper Python integration.
 *
 * @see GatewayServer
 * @since 1.0.0
 * @author w4t3rcs
 */
public class GatewayServerCreationException extends RuntimeException {
    /**
     * Constructs a new {@code GatewayServerCreationException} with the specified cause.
     *
     * @param cause the underlying cause of the failure (non-null)
     */
    public GatewayServerCreationException(Throwable cause) {
        super(cause);
    }
}
