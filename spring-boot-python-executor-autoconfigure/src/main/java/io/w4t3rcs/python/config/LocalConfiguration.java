package io.w4t3rcs.python.config;

import io.w4t3rcs.python.executor.LocalPythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.local.*;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot autoconfiguration for local Python process execution.
 * <p>
 * Declares beans required for running Python scripts via a {@link LocalPythonExecutor} in a separate local process.
 * This configuration is activated only when the property {@code spring.python.executor.type} is set to {@code local}.
 * </p>
 *
 * <p>
 * All beans are registered only if no other beans of the same type are already present in the application context (via {@link ConditionalOnMissingBean}).
 * This allows easy overriding in custom configurations.
 * </p>
 *
 * @see LocalPythonExecutor
 * @see PythonExecutorProperties.LocalProperties
 * @see ProcessStarter
 * @see ProcessHandler
 * @see ProcessFinisher
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
@ConditionalOnProperty(name = "spring.python.executor.type", havingValue = "local")
public class LocalConfiguration {
    /**
     * Creates the {@link ProcessStarter} bean for initializing and starting
     * local Python processes.
     *
     * <p>
     * The returned instance is based on {@link BasicPythonProcessStarter}, which uses the
     * provided {@link PythonExecutorProperties} and {@link PythonFileHandler}
     * to configure and manage process startup.
     * </p>
     *
     * @param executorProperties non-null execution settings for Python processes
     * @param pythonFileHandler non-null handler for managing Python files and scripts
     * @return a non-null {@link ProcessStarter} implementation
     */
    @Bean
    @ConditionalOnMissingBean(ProcessStarter.class)
    public ProcessStarter processStarter(PythonExecutorProperties executorProperties, PythonFileHandler pythonFileHandler) {
        return new BasicPythonProcessStarter(executorProperties, pythonFileHandler);
    }

    /**
     * Creates the {@link ProcessHandler} bean responsible for handling
     * standard input (stdin) communication with the Python process.
     *
     * <p>
     * The returned instance is based on {@link BasicPythonInputProcessHandler} and supports
     * passing string-based input to the running Python process.
     * </p>
     *
     * @param executorProperties non-null execution settings for Python processes
     * @param resolverProperties non-null configuration for Python resolvers
     * @return a non-null {@link ProcessHandler} implementation for input handling
     */
    @Bean
    @ConditionalOnMissingBean(BasicPythonInputProcessHandler.class)
    public ProcessHandler<String> inputProcessHandler(PythonExecutorProperties executorProperties, PythonResolverProperties resolverProperties) {
        return new BasicPythonInputProcessHandler(executorProperties, resolverProperties);
    }

    /**
     * Creates the {@link ProcessHandler} bean responsible for handling
     * standard error (stderr) output from the Python process.
     *
     * <p>
     * The returned instance is based on {@link BasicPythonErrorProcessHandler} and ignores input,
     * using {@link Void} as the generic type.
     * </p>
     *
     * @return a non-null {@link ProcessHandler} implementation for error handling
     */
    @Bean
    @ConditionalOnMissingBean(BasicPythonErrorProcessHandler.class)
    public ProcessHandler<Void> errorProcessHandler() {
        return new BasicPythonErrorProcessHandler();
    }

    /**
     * Creates the {@link ProcessFinisher} bean responsible for cleanly
     * terminating and releasing resources associated with the Python process.
     *
     * <p>
     * The returned instance is based on {@link BasicPythonProcessFinisher}.
     * </p>
     *
     * @return a non-null {@link ProcessFinisher} implementation
     */
    @Bean
    @ConditionalOnMissingBean(ProcessFinisher.class)
    public ProcessFinisher processFinisher() {
        return new BasicPythonProcessFinisher();
    }
}