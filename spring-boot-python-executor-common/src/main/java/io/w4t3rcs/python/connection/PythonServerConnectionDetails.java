package io.w4t3rcs.python.connection;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

/**
 * Defines connection parameters required to establish a link with a Python server.
 *
 * <p>This interface extends {@link ConnectionDetails} and provides
 * credentials and the target server URI in a structured manner. It is intended
 * for use in Spring Boot autoconfiguration and connection management components.</p>
 *
 * <p><strong>Thread safety:</strong> Implementations of this interface must be immutable
 * and thread-safe. All returned values must be consistent throughout the lifecycle of the object.</p>
 *
 * <p><strong>Null safety:</strong> All methods must return non-{@code null} values. Null parameters passed to
 * {@link #of(String, String)} will cause a {@link NullPointerException}.</p>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * PythonServerConnectionDetails details = PythonServerConnectionDetails.of(
 *     "secret-api-key",
 *     "http://localhost:8080"
 * );
 *
 * String uri = details.getUri();
 * }</pre>
 *
 * @see ConnectionDetails
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface PythonServerConnectionDetails extends ConnectionDetails {
    /**
     * Returns the token used for authenticating with the Python server.
     *
     * @return non-{@code null} token
     */
    String getToken();

    /**
     * Returns the URI of the target Python server.
     *
     * @return non-{@code null} server URI
     */
    String getUri();

    /**
     * Creates an immutable {@link PythonServerConnectionDetails} instance with the given parameters.
     *
     * <p>The returned instance is thread-safe and all values are stored as provided.
     * Null values are not allowed.</p>
     *
     * @param token non-{@code null} token
     * @param uri non-{@code null} URI of the Python server, including protocol and port if applicable
     * @return non-{@code null} {@link PythonServerConnectionDetails} instance
     * @throws NullPointerException if any parameter is {@code null}
     */
    static PythonServerConnectionDetails of(String token, String uri) {
        return new PythonServerConnectionDetails() {
            @Override
            public String getToken() {
                if (token == null) throw new NullPointerException("Token is null");
                return token;
            }

            @Override
            public String getUri() {
                if (uri == null) throw new NullPointerException("Uri is null");
                return uri;
            }
        };
    }
}