package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.cache.impl.HashCacheKeyGenerator;
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
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

/**
 * Autoconfiguration class for Python caching support.
 * <p>
 * This configuration activates caching components for Python integration based on the
 * {@code spring.python.cache.enabled} property and configured cache levels.
 * </p>
 * <p>
 * Note: Property {@code spring.python.cache.enabled} must go with {@link EnableCaching} annotation.
 * Without this annotation, caching won't work
 * </p>
 * <p>
 * Provides default {@link CacheKeyGenerator} and caching wrappers for
 * {@link PythonFileHandler}, {@link PythonResolverHolder}, {@link PythonExecutor}, and {@link PythonProcessor}
 * beans if present in the context.
 * </p>
 * <p>
 * Beans defined here are marked as {@code @Primary} to override default implementations
 * when caching is enabled.
 * </p>
 * <p>
 * The configuration reads default properties from {@code python-cache-default.properties}
 * located in the classpath.
 * </p>
 *
 * @see PythonCacheProperties
 * @see CacheKeyGenerator
 * @see CachingPythonFileHandler
 * @see CachingPythonResolverHolder
 * @see CachingPythonExecutor
 * @see CachingPythonProcessor
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(PythonCacheProperties.class)
@ConditionalOnProperty(name = "spring.python.cache.enabled", havingValue = "true")
@PropertySource("classpath:python-cache-default.properties")
public class PythonCacheAutoConfiguration {
    /**
     * Creates the default {@link CacheKeyGenerator} bean if none is present.
     * Uses {@link HashCacheKeyGenerator} configured by {@link PythonCacheProperties}.
     *
     * @param cacheProperties non-null configuration properties for Python cache
     * @return a new instance of {@link CacheKeyGenerator}
     */
    @Bean
    @ConditionalOnMissingBean(CacheKeyGenerator.class)
    public CacheKeyGenerator cacheKeyGenerator(PythonCacheProperties cacheProperties) {
        return new HashCacheKeyGenerator(cacheProperties);
    }

    /**
     * Wraps the existing {@link PythonFileHandler} with caching capabilities
     * when file cache level is enabled.
     *
     * @param cacheProperties non-null Python cache configuration properties
     * @param pythonFileHandler non-null delegate {@link PythonFileHandler} bean
     * @param cacheManager non-null Spring cache manager for cache resolution
     * @return a caching-enabled {@link PythonFileHandler} bean marked as primary
     */
    @Bean
    @Primary
    @ConditionalOnBean(PythonFileHandler.class)
    @Conditional(FileCacheLevelCondition.class)
    public PythonFileHandler cachingPythonFileHandler(PythonCacheProperties cacheProperties, PythonFileHandler pythonFileHandler, CacheManager cacheManager) {
        return new CachingPythonFileHandler(cacheProperties, pythonFileHandler, cacheManager);
    }

    /**
     * Wraps the existing {@link PythonResolverHolder} with caching capabilities
     * when resolver cache level is enabled.
     *
     * @param cacheProperties non-null Python cache configuration properties
     * @param pythonResolverHolder non-null delegate {@link PythonResolverHolder} bean
     * @param cacheManager non-null Spring cache manager
     * @param keyGenerator non-null cache key generator
     * @param objectMapper non-null JSON object mapper for serializing arguments
     * @return a caching-enabled {@link PythonResolverHolder} bean marked as primary
     */
    @Bean
    @Primary
    @ConditionalOnBean(PythonResolverHolder.class)
    @Conditional(ResolverCacheLevelCondition.class)
    public PythonResolverHolder cachingPythonResolverHolder(PythonCacheProperties cacheProperties, PythonResolverHolder pythonResolverHolder, CacheManager cacheManager, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        return new CachingPythonResolverHolder(cacheProperties, pythonResolverHolder, cacheManager, keyGenerator, objectMapper);
    }

    /**
     * Wraps the existing {@link PythonExecutor} with caching capabilities
     * when executor cache level is enabled.
     *
     * @param cacheProperties non-null Python cache configuration properties
     * @param pythonExecutor non-null delegate {@link PythonExecutor} bean
     * @param cacheManager non-null Spring cache manager
     * @param keyGenerator non-null cache key generator
     * @return a caching-enabled {@link PythonExecutor} bean marked as primary
     */
    @Bean
    @Primary
    @ConditionalOnBean(PythonExecutor.class)
    @Conditional(ExecutorCacheLevelCondition.class)
    public PythonExecutor cachingPythonExecutor(PythonCacheProperties cacheProperties, PythonExecutor pythonExecutor, CacheManager cacheManager, CacheKeyGenerator keyGenerator) {
        return new CachingPythonExecutor(cacheProperties, pythonExecutor, cacheManager, keyGenerator);
    }

    /**
     * Wraps the existing {@link PythonProcessor} with caching capabilities
     * when processor cache level is enabled.
     *
     * @param cacheProperties non-null Python cache configuration properties
     * @param pythonProcessor non-null delegate {@link PythonProcessor} bean
     * @param cacheManager non-null Spring cache manager
     * @param keyGenerator non-null cache key generator
     * @param objectMapper non-null JSON object mapper for serializing arguments
     * @return a caching-enabled {@link PythonProcessor} bean marked as primary
     */
    @Bean
    @Primary
    @ConditionalOnBean(PythonProcessor.class)
    @Conditional(ProcessorCacheLevelCondition.class)
    public PythonProcessor cachingPythonProcessor(PythonCacheProperties cacheProperties, PythonProcessor pythonProcessor, CacheManager cacheManager, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        return new CachingPythonProcessor(cacheProperties, pythonProcessor, cacheManager, keyGenerator, objectMapper);
    }
}