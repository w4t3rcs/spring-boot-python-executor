package io.w4t3rcs.python;

import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

public class PythonRestServerConnectionDetailsFactory extends ContainerConnectionDetailsFactory<PythonRestServerContainer, PythonRestServerConnectionDetailsFactory.PythonRestServerConnectionDetails> {
    @Override
    protected boolean sourceAccepts(ContainerConnectionSource<PythonRestServerContainer> source, Class<?> requiredContainerType, Class<?> requiredConnectionDetailsType) {
        return super.sourceAccepts(source, requiredContainerType, requiredConnectionDetailsType) || source.accepts(ANY_CONNECTION_NAME, PythonRestServerContainer.class, requiredConnectionDetailsType);
    }

    @Override
    protected PythonRestServerConnectionDetails getContainerConnectionDetails(ContainerConnectionSource<PythonRestServerContainer> source) {
        return new PythonRestServerConnectionDetails(source);
    }

    public static class PythonRestServerConnectionDetails extends ContainerConnectionDetails<PythonRestServerContainer> implements PythonServerConnectionDetails {
        protected PythonRestServerConnectionDetails(ContainerConnectionSource<PythonRestServerContainer> source) {
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
