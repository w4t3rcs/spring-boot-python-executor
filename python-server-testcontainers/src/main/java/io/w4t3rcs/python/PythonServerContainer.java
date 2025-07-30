package io.w4t3rcs.python;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class PythonServerContainer<SELF extends PythonServerContainer<SELF>> extends GenericContainer<SELF> {
    public static final String PYTHON_SERVER_USERNAME_ENV = "PYTHON_SERVER_USERNAME";
    public static final String PYTHON_SERVER_PASSWORD_ENV = "PYTHON_SERVER_PASSWORD";
    public static final String PYTHON_ADDITIONAL_IMPORTS_ENV = "PYTHON_ADDITIONAL_IMPORTS";
    public static final String PYTHON_ADDITIONAL_IMPORTS_DELIMITER_ENV = "PYTHON_ADDITIONAL_IMPORTS_DELIMITER";
    public static final String PYTHON_RESULT_APPEARANCE_ENV = "PYTHON_RESULT_APPEARANCE";
    protected static final String DEFAULT_USERNAME = "root";
    protected static final String DEFAULT_PASSWORD = "password";
    protected static final String DEFAULT_ADDITIONAL_IMPORTS_DELIMITER = ",";
    private String username = DEFAULT_USERNAME;
    private String password = DEFAULT_PASSWORD;

    public PythonServerContainer(String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    public PythonServerContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
        this.withUsername(DEFAULT_USERNAME).withPassword(DEFAULT_PASSWORD);
    }

    public SELF withUsername(String username) {
        this.withEnv(PYTHON_SERVER_USERNAME_ENV, username);
        this.username = username;
        return this.self();
    }

    public SELF withPassword(String password) {
        this.withEnv(PYTHON_SERVER_PASSWORD_ENV, password);
        this.password = password;
        return this.self();
    }

    public SELF withAdditionalImports(String[] imports) {
        return this.withAdditionalImports(imports, DEFAULT_ADDITIONAL_IMPORTS_DELIMITER);
    }

    public SELF withAdditionalImports(String[] imports, String delimiter) {
        this.withEnv(PYTHON_ADDITIONAL_IMPORTS_ENV, String.join(delimiter, imports));
        return this.self();
    }

    public SELF withAdditionalImportsDelimiter(String importsDelimiter) {
        this.withEnv(PYTHON_ADDITIONAL_IMPORTS_DELIMITER_ENV, importsDelimiter);
        return this.self();
    }

    public SELF withResultAppearance(String resultAppearance) {
        this.withEnv(PYTHON_RESULT_APPEARANCE_ENV, resultAppearance);
        return this.self();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
