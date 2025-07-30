package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.cache.impl.CacheKeyGeneratorImpl;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(PythonCacheProperties.class)
@ConditionalOnProperty(name = "spring.python.cache.enabled", havingValue = "true")
public class PythonCacheConfiguration {
    @Bean
    @ConditionalOnMissingBean(CacheKeyGenerator.class)
    public CacheKeyGenerator cacheKeyGenerator(PythonCacheProperties cacheProperties) {
        return new CacheKeyGeneratorImpl(cacheProperties);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonFileHandler.class)
    @ConditionalOnProperty(name = "spring.python.cache.file.enabled", havingValue = "true")
    public PythonFileHandler cachingPythonFileHandler(PythonCacheProperties cacheProperties, PythonFileHandler pythonFileHandler, CacheManager cacheManager) {
        return new CachingPythonFileHandler(cacheProperties, pythonFileHandler, cacheManager);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonResolverHolder.class)
    @ConditionalOnProperty(name = "spring.python.cache.level", havingValue = "resolver")
    public PythonResolverHolder cachingPythonResolverHolder(PythonCacheProperties cacheProperties, PythonResolverHolder pythonResolverHolder, CacheManager cacheManager, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        return new CachingPythonResolverHolder(cacheProperties, pythonResolverHolder, cacheManager, keyGenerator, objectMapper);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonExecutor.class)
    @ConditionalOnProperty(name = "spring.python.cache.level", havingValue = "executor")
    public PythonExecutor cachingPythonExecutor(PythonCacheProperties cacheProperties, PythonExecutor pythonExecutor, CacheManager cacheManager, CacheKeyGenerator keyGenerator) {
        return new CachingPythonExecutor(cacheProperties, pythonExecutor, cacheManager, keyGenerator);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonProcessor.class)
    @ConditionalOnProperty(name = "spring.python.cache.level", havingValue = "processor")
    public PythonProcessor cachingPythonProcessor(PythonCacheProperties cacheProperties, PythonProcessor pythonProcessor, CacheManager cacheManager, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        return new CachingPythonProcessor(cacheProperties, pythonProcessor, cacheManager, keyGenerator, objectMapper);
    }
}