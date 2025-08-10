package io.w4t3rcs.python.config;

import io.w4t3rcs.python.file.BasicPythonFileHandler;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.properties.PythonFileProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot autoconfiguration for {@link PythonFileHandler} beans.
 *
 * <p>This configuration registers a default {@link BasicPythonFileHandler} implementation
 * when no custom {@link PythonFileHandler} bean is already defined in the application context.</p>
 *
 * <p>It reads file-related settings from {@link PythonFileProperties} and uses them to initialize
 * the handler instance.</p>
 *
 * @see PythonFileHandler
 * @see BasicPythonFileHandler
 * @see PythonFileProperties
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(PythonFileProperties.class)
public class PythonFileHandlerConfiguration {
    /**
     * Creates a {@link BasicPythonFileHandler} with configuration from {@link PythonFileProperties}.
     *
     * @param fileProperties non-null configuration properties for file handling, must contain valid directory paths and permissions.
     * @return never {@code null}, an initialized instance of {@link BasicPythonFileHandler}.
     */
    @Bean
    @ConditionalOnMissingBean(PythonFileHandler.class)
    public PythonFileHandler basicPythonFileHandler(PythonFileProperties fileProperties) {
        return new BasicPythonFileHandler(fileProperties);
    }
}
