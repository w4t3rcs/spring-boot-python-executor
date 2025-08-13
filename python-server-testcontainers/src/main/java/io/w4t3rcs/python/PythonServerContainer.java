package io.w4t3rcs.python;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Base Testcontainers container for Python server.
 * <p>
 * Supports configuration of environment variables related to authentication,
 * additional imports, and result formatting via fluent API.
 * </p>
 * <p>
 * This generic class extends {@link GenericContainer} to enable type-safe
 * fluent API chaining returning the subclass type.
 * </p>
 * <p>
 * Default token: {@value #DEFAULT_TOKEN}
 * Default additional imports delimiter: {@value #DEFAULT_ADDITIONAL_IMPORTS_DELIMITER}
 * </p>
 *
 * <p>Environment variables used:</p>
 * <ul>
 *   <li>{@value #PYTHON_SERVER_TOKEN_ENV} — token for authentication</li>
 *   <li>{@value #PYTHON_ADDITIONAL_IMPORTS_ENV} — additional Python imports as a delimiter-separated string</li>
 *   <li>{@value #PYTHON_ADDITIONAL_IMPORTS_DELIMITER_ENV} — delimiter used in additional imports</li>
 *   <li>{@value #PYTHON_RESULT_APPEARANCE_ENV} — controls Python result appearance/formatting</li>
 * </ul>
 *
 * @param <SELF> self-type for fluent API returns
 * @see GenericContainer
 * @see PythonGrpcServerContainer
 * @see PythonRestServerContainer
 * @author w4t3rcs
 * @since 1.0.0
 */
public class PythonServerContainer<SELF extends PythonServerContainer<SELF>> extends GenericContainer<SELF> {
    public static final String PYTHON_SERVER_TOKEN_ENV = "PYTHON_SERVER_TOKEN";
    public static final String PYTHON_ADDITIONAL_IMPORTS_ENV = "PYTHON_ADDITIONAL_IMPORTS";
    public static final String PYTHON_ADDITIONAL_IMPORTS_DELIMITER_ENV = "PYTHON_ADDITIONAL_IMPORTS_DELIMITER";
    public static final String PYTHON_RESULT_APPEARANCE_ENV = "PYTHON_RESULT_APPEARANCE";
    protected static final String DEFAULT_TOKEN = "secret";
    protected static final String DEFAULT_ADDITIONAL_IMPORTS_DELIMITER = ",";
    private String token = DEFAULT_TOKEN;

    /**
     * Creates a container from the specified Docker image name string.
     *
     * @param dockerImageName Docker image name string, e.g. "my-python-server:latest"
     */
    public PythonServerContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    /**
     * Creates a container from the specified parsed Docker image name.
     *
     * @param dockerImageName parsed Docker image name
     */
    public PythonServerContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
        this.withToken(DEFAULT_TOKEN);
    }

    /**
     * Sets the authentication token for the Python server container and corresponding environment variable.
     *
     * @param token token to use
     * @return this container instance for chaining
     */
    public SELF withToken(String token) {
        this.withEnv(PYTHON_SERVER_TOKEN_ENV, token);
        this.token = token;
        return this.self();
    }

    /**
     * Sets additional Python imports with default delimiter (",").
     *
     * @param imports array of import strings
     * @return this container instance for chaining
     */
    public SELF withAdditionalImports(String[] imports) {
        return this.withAdditionalImports(imports, DEFAULT_ADDITIONAL_IMPORTS_DELIMITER);
    }

    /**
     * Sets additional Python imports joined by a custom delimiter.
     *
     * @param imports array of import strings
     * @param delimiter delimiter string to join imports
     * @return this container instance for chaining
     */
    public SELF withAdditionalImports(String[] imports, String delimiter) {
        this.withEnv(PYTHON_ADDITIONAL_IMPORTS_ENV, String.join(delimiter, imports));
        return this.self();
    }

    /**
     * Sets the delimiter to use for additional Python imports.
     *
     * @param importsDelimiter delimiter string
     * @return this container instance for chaining
     */
    public SELF withAdditionalImportsDelimiter(String importsDelimiter) {
        this.withEnv(PYTHON_ADDITIONAL_IMPORTS_DELIMITER_ENV, importsDelimiter);
        return this.self();
    }

    /**
     * Sets the result appearance option for the Python server.
     *
     * @param resultAppearance string defining result appearance format
     * @return this container instance for chaining
     */
    public SELF withResultAppearance(String resultAppearance) {
        this.withEnv(PYTHON_RESULT_APPEARANCE_ENV, resultAppearance);
        return this.self();
    }

    /**
     * Returns the currently configured token.
     *
     * @return token string
     */
    public String getToken() {
        return token;
    }
}