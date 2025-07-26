package io.w4t3rcs.python.config;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.cache.CacheKeyGeneratorImpl;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PythonCacheProperties.class)
@ConditionalOnProperty(name = "spring.python.cache.enabled", havingValue = "true")
public class PythonCacheConfiguration {
    @Bean
    public CacheKeyGenerator cacheKeyGenerator(PythonCacheProperties cacheProperties) {
        return new CacheKeyGeneratorImpl(cacheProperties);
    }
}
