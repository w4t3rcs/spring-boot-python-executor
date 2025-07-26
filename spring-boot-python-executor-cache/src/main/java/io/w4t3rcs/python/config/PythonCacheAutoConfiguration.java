package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.cache.CacheKeyGeneratorImpl;
import io.w4t3rcs.python.executor.CachingPythonExecutor;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.processor.CachingPythonProcessor;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableCaching
@EnableConfigurationProperties({
        PythonCacheProperties.class
})
@PropertySource("classpath:python-cache-default.properties")
@ConditionalOnProperty(name = "spring.python.cache.enabled", havingValue = "true")
public class PythonCacheAutoConfiguration {
    @Bean
    public CacheKeyGenerator cacheKeyGenerator(PythonCacheProperties cacheProperties) {
        return new CacheKeyGeneratorImpl(cacheProperties);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonExecutor.class)
    @ConditionalOnProperty(name = "spring.python.cache.level", havingValue = "executor")
    public PythonExecutor cachingPythonExecutor(PythonExecutor pythonExecutor, Cache pythonCache, CacheKeyGenerator keyGenerator) {
        return new CachingPythonExecutor(pythonExecutor, pythonCache, keyGenerator);
    }

    @Bean
    @Primary
    @ConditionalOnBean(PythonProcessor.class)
    @ConditionalOnProperty(name = "spring.python.cache.level", havingValue = "processor")
    public PythonProcessor cachingPythonProcessor(PythonProcessor pythonProcessor, Cache pythonCache, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        return new CachingPythonProcessor(pythonProcessor, pythonCache, keyGenerator, objectMapper);
    }

    @Bean
    public Cache pythonCache(PythonCacheProperties cacheProperties, CacheManager cacheManager) {
        return cacheManager.getCache(cacheProperties.name());
    }
}
