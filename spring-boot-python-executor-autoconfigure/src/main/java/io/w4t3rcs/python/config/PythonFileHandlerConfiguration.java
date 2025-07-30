package io.w4t3rcs.python.config;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.BasicPythonFileHandler;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.properties.PythonFileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Main configuration class for {@link PythonExecutor}.
 * This class sets up the core infrastructure for {@link PythonExecutor} bean declaration.
 */
@Configuration
@EnableConfigurationProperties(PythonFileProperties.class)
public class PythonFileHandlerConfiguration {
    @Bean
    @ConditionalOnMissingBean(PythonFileHandler.class)
    public PythonFileHandler basicPythonFileHandler(PythonFileProperties fileProperties) {
        return new BasicPythonFileHandler(fileProperties);
    }
}
