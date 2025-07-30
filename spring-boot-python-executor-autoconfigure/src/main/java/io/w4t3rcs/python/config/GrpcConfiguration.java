package io.w4t3rcs.python.config;

import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

/**
 * Configuration class for gRPC integration.
 * This class creates a gRPC client that enables communication between Java and Python.
 *
 * <p>The configuration is conditionally enabled based on the "spring.python.executor.type" property</p>
 */
@Configuration
@ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "grpc")
public class GrpcConfiguration {
    private static final String USERNAME_KEY = "x-username";
    private static final String PASSWORD_KEY = "x-password";

    @Bean
    @ConditionalOnMissingBean(PythonServiceGrpc.PythonServiceBlockingStub.class)
    public PythonServiceGrpc.PythonServiceBlockingStub stub(PythonServerConnectionDetails connectionDetails, GrpcChannelFactory channels) {
        ManagedChannel channel = channels.createChannel(connectionDetails.getUri());
        Metadata headers = new Metadata();
        var marshaller = Metadata.ASCII_STRING_MARSHALLER;
        Metadata.Key<String> usernameKey = Metadata.Key.of(USERNAME_KEY, marshaller);
        Metadata.Key<String> passwordKey = Metadata.Key.of(PASSWORD_KEY, marshaller);
        headers.put(usernameKey, connectionDetails.getUsername());
        headers.put(passwordKey, connectionDetails.getPassword());
        return PythonServiceGrpc.newBlockingStub(channel).withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers));
    }
}