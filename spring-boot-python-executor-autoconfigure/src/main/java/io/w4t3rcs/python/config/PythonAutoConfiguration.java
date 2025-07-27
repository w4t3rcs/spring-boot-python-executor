package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.aspect.PythonAspect;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.file.impl.BasicPythonFileHandler;
import io.w4t3rcs.python.file.impl.CachingPythonFileHandler;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.processor.impl.BasicPythonProcessor;
import io.w4t3rcs.python.processor.impl.CachingPythonProcessor;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import io.w4t3rcs.python.properties.PythonFileProperties;
import io.w4t3rcs.python.resolver.PythonResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
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
        PythonResolverConfiguration.class,
        PythonCacheConfiguration.class,
})
@PropertySource("classpath:python-default.properties")
public class PythonAutoConfiguration {
    @Bean
    public PythonFileHandler basicPythonFileHandler(PythonFileProperties fileProperties) {
        return new BasicPythonFileHandler(fileProperties);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonFileHandler.class)
    @ConditionalOnProperty(name = "spring.python.cache.enabled", havingValue = "true")
    public PythonFileHandler pythonFileHandler(PythonCacheProperties cacheProperties, PythonFileHandler pythonFileHandler, CacheManager cacheManager) {
        return new CachingPythonFileHandler(cacheProperties, pythonFileHandler, cacheManager);
    }

    @Bean
    public PythonProcessor basicPythonProcessor(PythonFileHandler pythonFileHandler, PythonExecutor pythonExecutor, List<PythonResolver> pythonResolvers) {
        return new BasicPythonProcessor(pythonFileHandler, pythonExecutor, pythonResolvers);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonProcessor.class)
    @ConditionalOnProperties({
            @ConditionalOnProperty(name = "spring.python.cache.enabled", havingValue = "true"),
            @ConditionalOnProperty(name = "spring.python.cache.level", havingValue = "processor")
    })
    public PythonProcessor cachingPythonProcessor(PythonCacheProperties cacheProperties, PythonProcessor pythonProcessor, CacheManager cacheManager, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        return new CachingPythonProcessor(cacheProperties, pythonProcessor, cacheManager, keyGenerator, objectMapper);
    }

    @Bean
    public PythonAspect pythonAspect(PythonProcessor pythonProcessor) {
        return new PythonAspect(pythonProcessor);
    }
}
