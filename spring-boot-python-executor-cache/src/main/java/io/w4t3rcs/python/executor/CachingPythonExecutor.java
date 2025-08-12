package io.w4t3rcs.python.executor;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.dto.PythonExecutionResponse;
import io.w4t3rcs.python.exception.PythonCacheException;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * {@link PythonExecutor} implementation that adds caching capabilities.
 * <p>
 * This executor delegates script execution to a wrapped {@link PythonExecutor} instance,
 * caching results based on generated cache keys to improve performance on repeated executions
 * with identical scripts and body types.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * PythonExecutor baseExecutor = ...;
 * CacheKeyGenerator keyGen = ...;
 * CacheManager cacheManager = ...;
 * PythonCacheProperties cacheProps = ...;
 * PythonExecutor cachingExecutor = new CachingPythonExecutor(cacheProps, baseExecutor, cacheManager, keyGen);
 *
 * String script = "print('hello')";
 * cachingExecutor.execute(script, null);
 * }</pre>
 *
 * @see PythonExecutor
 * @see PythonCacheProperties.NameProperties
 * @see CacheKeyGenerator
 * @author w4t3rcs
 * @since 1.0.0
 */
public class CachingPythonExecutor implements PythonExecutor {
    private final PythonExecutor pythonExecutor;
    private final Cache cache;
    private final CacheKeyGenerator keyGenerator;

    /**
     * Constructs a new {@code CachingPythonExecutor}.
     *
     * @param cacheProperties non-null properties to configure caching, must provide a valid cache name
     * @param pythonExecutor non-null delegate {@link PythonExecutor} for actual script execution
     * @param cacheManager non-null {@link CacheManager} used to obtain the {@link Cache} instance
     * @param keyGenerator non-null {@link CacheKeyGenerator} for generating cache keys
     */
    public CachingPythonExecutor(PythonCacheProperties cacheProperties, PythonExecutor pythonExecutor, CacheManager cacheManager, CacheKeyGenerator keyGenerator) {
        this.pythonExecutor = pythonExecutor;
        this.cache = cacheManager.getCache(cacheProperties.name().executor());
        this.keyGenerator = keyGenerator;
    }

    /**
     * Executes the given Python script and returns the body of the specified type.
     * <p>
     * If a cached body is available for the generated cache key, it is returned immediately.
     * Otherwise, the script is executed using the delegate {@link PythonExecutor},
     * and the body is cached before returning.
     * </p>
     * <p>
     * Cache keys are generated using the configured {@link CacheKeyGenerator} with the script and body class.
     * </p>
     *
     * @param <R> the expected body type
     * @param script non-null Python script to execute
     * @param resultClass non-null {@link Class} representing the expected body type
     * @return the execution body, guaranteed non-null if the delegate returns non-null
     * @throws PythonCacheException if any caching or execution error occurs
     */
    @Override
    @SuppressWarnings("unchecked")
    public <R> PythonExecutionResponse<R> execute(String script, Class<? extends R> resultClass) {
        try {
            String key = keyGenerator.generateKey(script, resultClass);
            PythonExecutionResponse<R> cachedResult = (PythonExecutionResponse<R>) cache.get(key, PythonExecutionResponse.class);
            if (cachedResult != null) {
                return cachedResult;
            } else {
                PythonExecutionResponse<R> result = pythonExecutor.execute(script, resultClass);
                cache.put(key, result);
                return result;
            }
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }
}