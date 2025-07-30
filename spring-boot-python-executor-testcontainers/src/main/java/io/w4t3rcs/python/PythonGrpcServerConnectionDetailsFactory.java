package io.w4t3rcs.python;

import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

public class PythonGrpcServerConnectionDetailsFactory extends ContainerConnectionDetailsFactory<PythonGrpcServerContainer, PythonGrpcServerConnectionDetailsFactory.PythonGrpcServerConnectionDetails> {
    @Override
    protected boolean sourceAccepts(ContainerConnectionSource<PythonGrpcServerContainer> source, Class<?> requiredContainerType, Class<?> requiredConnectionDetailsType) {
        return super.sourceAccepts(source, requiredContainerType, requiredConnectionDetailsType) || source.accepts(ANY_CONNECTION_NAME, PythonGrpcServerContainer.class, requiredConnectionDetailsType);
    }

    @Override
    protected PythonGrpcServerConnectionDetails getContainerConnectionDetails(ContainerConnectionSource<PythonGrpcServerContainer> source) {
        return new PythonGrpcServerConnectionDetails(source);
    }

    public static class PythonGrpcServerConnectionDetails extends ContainerConnectionDetailsFactory.ContainerConnectionDetails<PythonGrpcServerContainer> implements PythonServerConnectionDetails {
        protected PythonGrpcServerConnectionDetails(ContainerConnectionSource<PythonGrpcServerContainer> source) {
            super(source);
        }

        @Override
        public String getUsername() {
            return this.getContainer().getUsername();
        }

        @Override
        public String getPassword() {
            return this.getContainer().getPassword();
        }

        @Override
        public String getUri() {
            return this.getContainer().getServerUrl();
        }
    }
}
