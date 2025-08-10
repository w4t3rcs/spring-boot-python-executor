package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.connection.PythonServerConnectionDetails;
import io.w4t3rcs.python.executor.GrpcPythonExecutor;
import io.w4t3rcs.python.executor.LocalPythonExecutor;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.executor.RestPythonExecutor;
import io.w4t3rcs.python.local.ProcessFinisher;
import io.w4t3rcs.python.local.ProcessHandler;
import io.w4t3rcs.python.local.ProcessStarter;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

/**
 * Central Spring Boot configuration for creating and wiring {@link PythonExecutor} beans.
 *
 * <p>This class declares {@link org.springframework.context.annotation.Bean} methods
 * for all supported execution types:
 * <ul>
 *   <li>Local process execution via {@link LocalPythonExecutor}</li>
 *   <li>Remote execution via REST API using {@link RestPythonExecutor}</li>
 *   <li>Remote execution via gRPC using {@link GrpcPythonExecutor}</li>
 * </ul>
 *
 * <p>Execution type is selected via the property:
 * <pre>{@code spring.python.executor.type=local|rest|grpc}</pre>
 * If the property is not set, {@code local} execution is used by default.</p>
 *
 * @see PythonExecutor
 * @see LocalPythonExecutor
 * @see RestPythonExecutor
 * @see GrpcPythonExecutor
 * @see PythonExecutorProperties
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(PythonExecutorProperties.class)
public class PythonExecutorConfiguration {
    /**
     * Creates a {@link LocalPythonExecutor} bean for executing Python scripts locally.
     *
     * <p>Activated when:
     * <ul>
     *   <li>{@code spring.python.executor.type=local}</li>
     *   <li>No other {@link PythonExecutor} bean is present in the context</li>
     * </ul>
     *
     * @param processStarter non-null {@link ProcessStarter} for launching Python processes
     * @param inputProcessHandler non-null {@link ProcessHandler} for handling process input
     * @param errorProcessHandler non-null {@link ProcessHandler} for handling process error output
     * @param objectMapper non-null {@link ObjectMapper} for JSON serialization/deserialization
     * @param processFinisher non-null {@link ProcessFinisher} for finalizing process execution
     * @return never {@code null}, fully initialized {@link LocalPythonExecutor} instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonExecutor.class)
    @ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "local", matchIfMissing = true)
    public PythonExecutor localPythonExecutor(ProcessStarter processStarter,
                                              ProcessHandler<String> inputProcessHandler,
                                              ProcessHandler<Void> errorProcessHandler,
                                              ObjectMapper objectMapper,
                                              ProcessFinisher processFinisher) {
        return new LocalPythonExecutor(processStarter, inputProcessHandler, errorProcessHandler, objectMapper, processFinisher);
    }

    /**
     * Creates a {@link RestPythonExecutor} bean for executing Python scripts via REST API.
     *
     * <p>Activated when:
     * <ul>
     *   <li>{@code spring.python.executor.type=rest}</li>
     *   <li>No other {@link PythonExecutor} bean is present in the context</li>
     * </ul>
     *
     * @param connectionDetails non-null {@link PythonServerConnectionDetails} for REST server connection
     * @param objectMapper non-null {@link ObjectMapper} for JSON serialization/deserialization
     * @param restPythonServerHttpClient non-null {@link HttpClient} for HTTP communication with the Python server
     * @return never {@code null}, fully initialized {@link RestPythonExecutor} instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonExecutor.class)
    @ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "rest")
    public PythonExecutor restPythonExecutor(PythonServerConnectionDetails connectionDetails,
                                             ObjectMapper objectMapper,
                                             @Qualifier("restPythonServerHttpClient") HttpClient restPythonServerHttpClient) {
        return new RestPythonExecutor(connectionDetails, objectMapper, restPythonServerHttpClient);
    }

    /**
     * Creates a default {@link HttpClient} bean for REST-based Python execution.
     *
     * <p>Activated when:
     * <ul>
     *   <li>{@code spring.python.executor.type=rest}</li>
     * </ul>
     *
     * @return never {@code null}, new {@link HttpClient} instance
     */
    @Bean
    @ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "rest")
    public HttpClient restPythonServerHttpClient() {
        return HttpClient.newHttpClient();
    }

    /**
     * Creates {@link PythonServerConnectionDetails} for REST Python execution from
     * {@link PythonExecutorProperties.RestProperties}.
     *
     * <p>Activated when:
     * <ul>
     *   <li>{@code spring.python.executor.type=rest}</li>
     *   <li>No other {@link PythonServerConnectionDetails} bean is present</li>
     * </ul>
     *
     * @param properties non-null {@link PythonExecutorProperties} containing REST configuration
     * @return never {@code null}, immutable connection details instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonServerConnectionDetails.class)
    @ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "rest")
    public PythonServerConnectionDetails restConnectionDetails(PythonExecutorProperties properties) {
        PythonExecutorProperties.RestProperties restProperties = properties.rest();
        return PythonServerConnectionDetails.of(restProperties.username(), restProperties.password(), restProperties.uri());
    }

    /**
     * Creates a {@link GrpcPythonExecutor} bean for executing Python scripts via gRPC.
     *
     * <p>Activated when:
     * <ul>
     *   <li>{@code spring.python.executor.type=grpc}</li>
     *   <li>No other {@link PythonExecutor} bean is present in the context</li>
     * </ul>
     *
     * @param stub non-null {@link PythonServiceGrpc.PythonServiceBlockingStub} for gRPC communication
     * @param objectMapper non-null {@link ObjectMapper} for JSON serialization/deserialization
     * @return never {@code null}, fully initialized {@link GrpcPythonExecutor} instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonExecutor.class)
    @ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "grpc")
    public PythonExecutor grpcPythonExecutor(PythonServiceGrpc.PythonServiceBlockingStub stub, ObjectMapper objectMapper) {
        return new GrpcPythonExecutor(stub, objectMapper);
    }

    /**
     * Creates {@link PythonServerConnectionDetails} for gRPC Python execution from
     * {@link PythonExecutorProperties.GrpcProperties}.
     *
     * <p>Activated when:
     * <ul>
     *   <li>{@code spring.python.executor.type=grpc}</li>
     *   <li>No other {@link PythonServerConnectionDetails} bean is present</li>
     * </ul>
     *
     * @param properties non-null {@link PythonExecutorProperties} containing gRPC configuration
     * @return never {@code null}, immutable connection details instance
     */
    @Bean
    @ConditionalOnMissingBean(PythonServerConnectionDetails.class)
    @ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "grpc")
    public PythonServerConnectionDetails grpcConnectionDetails(PythonExecutorProperties properties) {
        PythonExecutorProperties.GrpcProperties grpcProperties = properties.grpc();
        return PythonServerConnectionDetails.of(grpcProperties.username(), grpcProperties.password(), grpcProperties.uri());
    }
}