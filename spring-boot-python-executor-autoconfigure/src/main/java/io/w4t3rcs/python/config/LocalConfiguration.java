package io.w4t3rcs.python.config;

import io.w4t3rcs.python.executor.impl.LocalPythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.local.ProcessFinisher;
import io.w4t3rcs.python.local.ProcessHandler;
import io.w4t3rcs.python.local.ProcessStarter;
import io.w4t3rcs.python.local.impl.ErrorProcessHandler;
import io.w4t3rcs.python.local.impl.InputProcessHandler;
import io.w4t3rcs.python.local.impl.ProcessFinisherImpl;
import io.w4t3rcs.python.local.impl.ProcessStarterImpl;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for local process integration.
 * This class creates a necessary beans for {@link LocalPythonExecutor}.
 *
 * <p>The configuration is conditionally enabled based on the "spring.python.executor.type" property</p>
 */
@Configuration
@ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "local")
public class LocalConfiguration {
    @Bean
    public ProcessStarter processStarter(PythonExecutorProperties executorProperties, PythonFileHandler pythonFileHandler) {
        return new ProcessStarterImpl(executorProperties, pythonFileHandler);
    }

    @Bean
    public ProcessHandler<String> inputProcessHandler(PythonExecutorProperties executorProperties, PythonResolverProperties resolverProperties) {
        return new InputProcessHandler(executorProperties, resolverProperties);
    }

    @Bean
    public ProcessHandler<Void> errorProcessHandler() {
        return new ErrorProcessHandler();
    }

    @Bean
    public ProcessFinisher processFinisher() {
        return new ProcessFinisherImpl();
    }
}