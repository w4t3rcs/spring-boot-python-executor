package io.w4t3rcs.python.config;

import io.w4t3rcs.python.aspect.PythonAspect;
import io.w4t3rcs.python.processor.PythonProcessor;
import org.springframework.context.annotation.*;

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
        PythonCacheConfiguration.class
})
@PropertySource("classpath:python-default.properties")
public class PythonAutoConfiguration {
    @Bean
    public PythonAspect pythonAspect(PythonProcessor pythonProcessor) {
        return new PythonAspect(pythonProcessor);
    }
}
