package io.w4t3rcs.python.config;

import io.w4t3rcs.python.aspect.PythonAspect;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.file.impl.PythonFileHandlerImpl;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.processor.impl.PythonProcessorImpl;
import io.w4t3rcs.python.properties.PythonProperties;
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
 *   <li>Component scanning for all classes in the io.w4t3rcs.python package</li>
 *   <li>Aspect-oriented programming support for Python script execution via annotations</li>
 *   <li>Configuration properties for Python execution, SpEL integration, and result processing</li>
 * </ul>
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(PythonProperties.class)
@Import({GrpcConfig.class, LocalConfig.class, Py4JConfig.class, PythonExecutorConfig.class, PythonResolverConfig.class})
public class PythonConfig {
    @Bean
    public PythonFileHandler pythonFileHandler(PythonProperties pythonProperties) {
        return new PythonFileHandlerImpl(pythonProperties);
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
