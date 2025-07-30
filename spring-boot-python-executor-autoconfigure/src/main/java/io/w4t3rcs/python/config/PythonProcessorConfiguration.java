package io.w4t3rcs.python.config;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.processor.BasicPythonProcessor;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.resolver.PythonResolverHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class for {@link PythonExecutor}.
 * This class sets up the core infrastructure for {@link PythonExecutor} bean declaration.
 */
@Configuration
public class PythonProcessorConfiguration {
    @Bean
    @ConditionalOnMissingBean(PythonProcessor.class)
    public PythonProcessor basicPythonProcessor(PythonFileHandler pythonFileHandler, PythonExecutor pythonExecutor, PythonResolverHolder pythonResolverHolder) {
        return new BasicPythonProcessor(pythonFileHandler, pythonExecutor, pythonResolverHolder);
    }
}
