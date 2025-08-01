package io.w4t3rcs.python.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Main configuration class for Python integration.
 * This class sets up the core infrastructure for executing Python scripts from Java.
 * 
 * <p>It enables:</p>
 * <ul>
 *   <li>Importing necessary configuration classes</li>
 *   <li>Aspect-oriented programming support for Python script execution via annotations</li>
 * </ul>
 */
@Configuration
@EnableAspectJAutoProxy
@Import({
        GrpcConfiguration.class,
        LocalConfiguration.class,
        Py4JConfiguration.class,
        PythonFileHandlerConfiguration.class,
        PythonResolverConfiguration.class,
        PythonExecutorConfiguration.class,
        PythonProcessorConfiguration.class,
        PythonAspectConfiguration.class
})
@PropertySource("classpath:python-default.properties")
public class PythonAutoConfiguration {
}
