package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.executor.impl.GrpcPythonExecutor;
import io.w4t3rcs.python.executor.impl.LocalPythonExecutor;
import io.w4t3rcs.python.executor.impl.RestPythonExecutor;
import io.w4t3rcs.python.local.ProcessFinisher;
import io.w4t3rcs.python.local.ProcessHandler;
import io.w4t3rcs.python.local.ProcessStarter;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import io.w4t3rcs.python.proto.PythonServiceGrpc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class for {@link PythonExecutor}.
 * This class sets up the core infrastructure for {@link PythonExecutor} bean declaration.
 */
@Configuration
@EnableConfigurationProperties(PythonExecutorProperties.class)
public class PythonExecutorConfiguration {
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

    @Bean
    @ConditionalOnMissingBean(PythonExecutor.class)
    @ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "rest")
    public PythonExecutor restPythonExecutor(PythonExecutorProperties properties, ObjectMapper objectMapper) {
        return new RestPythonExecutor(properties, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(PythonExecutor.class)
    @ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "grpc")
    public PythonExecutor grpcPythonExecutor(PythonServiceGrpc.PythonServiceBlockingStub stub, ObjectMapper objectMapper) {
        return new GrpcPythonExecutor(stub, objectMapper);
    }
}
