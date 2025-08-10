package io.w4t3rcs.python.config;

import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import io.w4t3rcs.python.executor.GrpcPythonExecutor;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

/**
 * Provides Spring Boot autoconfiguration for gRPC-based {@link PythonServiceGrpc} integration.
 * <p>
 * This configuration declares a {@link PythonServiceGrpc.PythonServiceBlockingStub} bean
 * for synchronous RPC communication between Java and Python services over gRPC.
 * It is activated only when the property
 * {@code spring.python.executor.type=grpc} is present in the application environment.
 * </p>
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Establishes a {@link io.grpc.ManagedChannel} using {@link GrpcChannelFactory}.</li>
 *   <li>Ensures the bean is created only if no other {@link PythonServiceGrpc.PythonServiceBlockingStub} is already defined.</li>
 * </ul>
 *
 * @see GrpcPythonExecutor
 * @see PythonExecutorProperties.GrpcProperties
 * @see PythonServiceGrpc
 * @see GrpcChannelFactory
 * @see PythonServerConnectionDetails
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "grpc")
public class GrpcConfiguration {
    /**
     * HTTP header name used for passing the username in gRPC metadata.
     * <p>This header is attached to every outgoing gRPC request.</p>
     */
    public static final String USERNAME_KEY = "x-username";
    /**
     * HTTP header name used for passing the password in gRPC metadata.
     * <p>This header is attached to every outgoing gRPC request.</p>
     */
    public static final String PASSWORD_KEY = "x-password";

    /**
     * Creates a {@link PythonServiceGrpc.PythonServiceBlockingStub} bean configured with:
     * <ul>
     *   <li>Managed channel targeting the Python gRPC server URI</li>
     *   <li>Authentication metadata containing username and password</li>
     * </ul>
     *
     * <p>The bean is only created if:</p>
     * <ul>
     *   <li>{@code spring.python.executor.type=grpc}</li>
     *   <li>No other {@link PythonServiceGrpc.PythonServiceBlockingStub} bean exists in the context</li>
     * </ul>
     *
     * @param connectionDetails non-null connection configuration, including URI, username, and password
     * @param channels non-null {@link GrpcChannelFactory} for creating managed gRPC channels
     * @return non-null gRPC blocking stub ready for synchronous communication with the Python service
     */
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
        return PythonServiceGrpc.newBlockingStub(channel)
                .withInterceptors(MetadataUtils.newAttachHeadersInterceptor(headers));
    }
}