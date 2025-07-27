package io.w4t3rcs.python.executor.impl;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.exception.PythonCacheException;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;

@RequiredArgsConstructor
public class CachingPythonExecutor implements PythonExecutor {
    private final PythonCacheProperties cacheProperties;
    private final PythonExecutor pythonExecutor;
    private final CacheManager cacheManager;
    private final CacheKeyGenerator keyGenerator;

    @Override
    public <R> R execute(String script, Class<? extends R> resultClass) {
        try {
            String key = keyGenerator.generateKey(null, script, resultClass.getName());
            Cache cache = cacheManager.getCache(cacheProperties.name());
            R cachedResult = Objects.requireNonNull(cache).get(key, resultClass);
            if (cachedResult != null) {
                return cachedResult;
            } else {
                R result = pythonExecutor.execute(script, resultClass);
                cache.put(key, result);
                return result;
            }
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }
}
