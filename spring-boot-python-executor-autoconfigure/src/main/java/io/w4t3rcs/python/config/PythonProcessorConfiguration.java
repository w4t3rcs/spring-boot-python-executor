package io.w4t3rcs.python.config;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.processor.BasicPythonProcessor;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.resolver.PythonResolverHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Autoconfiguration class for {@link PythonProcessor}.
 * <p>This configuration defines the core infrastructure by declaring a {@link PythonProcessor} bean.</p>
 *
 * <p>If no other {@link PythonProcessor} bean is present in the Spring context,
 * it creates a {@link BasicPythonProcessor} instance wired with the required dependencies.</p>
 *
 * @see PythonProcessor
 * @see BasicPythonProcessor
 * @see PythonFileHandler
 * @see PythonExecutor
 * @see PythonResolver
 * @see PythonResolverHolder
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
public class PythonProcessorConfiguration {
    /**
     * Creates a default {@link BasicPythonProcessor} bean.
     *
     * @param pythonFileHandler non-null {@link PythonFileHandler} instance to handle Python file operations.
     * @param pythonExecutor non-null {@link PythonExecutor} instance to execute Python code.
     * @param pythonResolverHolder non-null {@link PythonResolverHolder} instance to resolve Python-related parameters.
     * @return a non-null {@link PythonProcessor} implementation.
     */
    @Bean
    @ConditionalOnMissingBean(PythonProcessor.class)
    public PythonProcessor basicPythonProcessor(PythonFileHandler pythonFileHandler, PythonExecutor pythonExecutor, PythonResolverHolder pythonResolverHolder) {
        return new BasicPythonProcessor(pythonFileHandler, pythonExecutor, pythonResolverHolder);
    }
}
