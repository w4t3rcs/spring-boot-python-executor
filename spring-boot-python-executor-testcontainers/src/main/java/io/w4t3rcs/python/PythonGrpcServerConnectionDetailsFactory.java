package io.w4t3rcs.python;

import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

/**
 * Factory for creating {@link PythonGrpcServerConnectionDetails} from a running
 * {@link PythonGrpcServerContainer} instance within a Spring Boot Testcontainers
 * environment.
 *
 * <p>This class integrates with Spring Boot's {@code ContainerConnectionDetailsFactory}
 * mechanism to automatically supply connection details (URI, username, password)
 * for a gRPC-based Python server running in a Testcontainer. It supports both the
 * standard container type matching and the generic {@code ANY_CONNECTION_NAME} convention.</p>
 *
 * <p>Usage in Spring Boot Tests</p>
 * <pre>{@code
 * @ServiceConnection
 * static PythonGrpcServerContainer grpc = ...;
 * }</pre>
 *
 * @see PythonGrpcServerContainer
 * @see PythonServerConnectionDetails
 * @see ContainerConnectionDetailsFactory
 * @author w4t3rcs
 * @since 1.0.0
 */
public class PythonGrpcServerConnectionDetailsFactory extends ContainerConnectionDetailsFactory<PythonGrpcServerContainer, PythonGrpcServerConnectionDetailsFactory.PythonGrpcServerConnectionDetails> {
    /**
     * Determines whether the given {@link ContainerConnectionSource} can be used
     * to create connection details for a {@link PythonGrpcServerContainer}.
     *
     * <p>Matches if the required container type and connection details type
     * are compatible, or if the container source matches the
     * {@code ANY_CONNECTION_NAME} convention for a {@link PythonGrpcServerContainer}.</p>
     *
     * @param source container connection source
     * @param requiredContainerType the expected container type
     * @param requiredConnectionDetailsType the expected connection details type
     * @return {@code true} if this factory can handle the given source
     */
    @Override
    protected boolean sourceAccepts(ContainerConnectionSource<PythonGrpcServerContainer> source, Class<?> requiredContainerType, Class<?> requiredConnectionDetailsType) {
        return super.sourceAccepts(source, requiredContainerType, requiredConnectionDetailsType) || source.accepts(ANY_CONNECTION_NAME, PythonGrpcServerContainer.class, requiredConnectionDetailsType);
    }

    /**
     * Creates {@link PythonGrpcServerConnectionDetails} from the given container source.
     *
     * @param source the container connection source
     * @return a non-null connection details object
     */
    @Override
    protected PythonGrpcServerConnectionDetails getContainerConnectionDetails(ContainerConnectionSource<PythonGrpcServerContainer> source) {
        return new PythonGrpcServerConnectionDetails(source);
    }

    /**
     * Connection details for a {@link PythonGrpcServerContainer}.
     *
     * <p>Provides access to the gRPC server's connection parameters such as
     * URI, username, and password by delegating to the running container instance.</p>
     */
    public static class PythonGrpcServerConnectionDetails extends ContainerConnectionDetailsFactory.ContainerConnectionDetails<PythonGrpcServerContainer> implements PythonServerConnectionDetails {
        /**
         * Creates a new {@code PythonGrpcServerConnectionDetails} for the given source.
         *
         * @param source non-null container connection source
         */
        protected PythonGrpcServerConnectionDetails(ContainerConnectionSource<PythonGrpcServerContainer> source) {
            super(source);
        }

        /** {@inheritDoc} */
        @Override
        public String getUsername() {
            return this.getContainer().getUsername();
        }

        /** {@inheritDoc} */
        @Override
        public String getPassword() {
            return this.getContainer().getPassword();
        }

        /** {@inheritDoc} */
        @Override
        public String getUri() {
            return this.getContainer().getServerUrl();
        }
    }
}