package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.cache.impl.CacheKeyGeneratorImpl;
import io.w4t3rcs.python.condition.ExecutorCacheLevelCondition;
import io.w4t3rcs.python.condition.FileCacheLevelCondition;
import io.w4t3rcs.python.condition.ProcessorCacheLevelCondition;
import io.w4t3rcs.python.condition.ResolverCacheLevelCondition;
import io.w4t3rcs.python.executor.CachingPythonExecutor;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.CachingPythonFileHandler;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.processor.CachingPythonProcessor;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import io.w4t3rcs.python.resolver.CachingPythonResolverHolder;
import io.w4t3rcs.python.resolver.PythonResolverHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.*;

@Configuration
@EnableConfigurationProperties(PythonCacheProperties.class)
@ConditionalOnProperty(name = "spring.python.cache.enabled", havingValue = "true")
@PropertySource("classpath:python-cache-default.properties")
public class PythonCacheAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CacheKeyGenerator.class)
    public CacheKeyGenerator cacheKeyGenerator(PythonCacheProperties cacheProperties) {
        return new CacheKeyGeneratorImpl(cacheProperties);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonFileHandler.class)
    @Conditional(FileCacheLevelCondition.class)
    public PythonFileHandler cachingPythonFileHandler(PythonCacheProperties cacheProperties, PythonFileHandler pythonFileHandler, CacheManager cacheManager) {
        return new CachingPythonFileHandler(cacheProperties, pythonFileHandler, cacheManager);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonResolverHolder.class)
    @Conditional(ResolverCacheLevelCondition.class)
    public PythonResolverHolder cachingPythonResolverHolder(PythonCacheProperties cacheProperties, PythonResolverHolder pythonResolverHolder, CacheManager cacheManager, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        return new CachingPythonResolverHolder(cacheProperties, pythonResolverHolder, cacheManager, keyGenerator, objectMapper);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonExecutor.class)
    @Conditional(ExecutorCacheLevelCondition.class)
    public PythonExecutor cachingPythonExecutor(PythonCacheProperties cacheProperties, PythonExecutor pythonExecutor, CacheManager cacheManager, CacheKeyGenerator keyGenerator) {
        return new CachingPythonExecutor(cacheProperties, pythonExecutor, cacheManager, keyGenerator);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonProcessor.class)
    @Conditional(ProcessorCacheLevelCondition.class)
    public PythonProcessor cachingPythonProcessor(PythonCacheProperties cacheProperties, PythonProcessor pythonProcessor, CacheManager cacheManager, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        return new CachingPythonProcessor(cacheProperties, pythonProcessor, cacheManager, keyGenerator, objectMapper);
    }
}