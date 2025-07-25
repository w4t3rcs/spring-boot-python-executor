package io.w4t3rcs.python.config;

import io.w4t3rcs.python.aspect.PythonAspect;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.file.impl.BasicPythonFileHandler;
import io.w4t3rcs.python.file.impl.CachingBasicPythonFileHandler;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.processor.impl.PythonProcessorImpl;
import io.w4t3rcs.python.properties.PythonFileProperties;
import io.w4t3rcs.python.resolver.PythonResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import java.util.List;

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
@EnableConfigurationProperties({
        PythonFileProperties.class
})
@Import({
        GrpcConfiguration.class,
        LocalConfiguration.class,
        Py4JConfiguration.class,
        PythonExecutorConfiguration.class,
        PythonResolverConfiguration.class
})
@PropertySource("classpath:python-default.properties")
public class PythonAutoConfiguration {
    @Bean
    public PythonFileHandler pythonFileHandler(PythonFileProperties fileProperties) {
        return fileProperties.cacheable() ? new CachingBasicPythonFileHandler(fileProperties) : new BasicPythonFileHandler(fileProperties);
    }

    @Bean
    public PythonProcessor pythonProcessor(PythonFileHandler pythonFileHandler, PythonExecutor pythonExecutor, List<PythonResolver> pythonResolvers) {
        return new PythonProcessorImpl(pythonFileHandler, pythonExecutor, pythonResolvers);
    }

    @Bean
    public PythonAspect pythonAspect(PythonProcessor pythonProcessor) {
        return new PythonAspect(pythonProcessor);
    }
}
