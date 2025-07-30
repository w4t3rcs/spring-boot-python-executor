package io.w4t3rcs.python.executor;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.exception.PythonCacheException;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;

public class CachingPythonExecutor implements PythonExecutor {
    private final PythonExecutor pythonExecutor;
    private final Cache cache;
    private final CacheKeyGenerator keyGenerator;

    public CachingPythonExecutor(PythonCacheProperties cacheProperties, PythonExecutor pythonExecutor, CacheManager cacheManager, CacheKeyGenerator keyGenerator) {
        this.pythonExecutor = pythonExecutor;
        this.cache = cacheManager.getCache(cacheProperties.name());
        this.keyGenerator = keyGenerator;
    }

    @Override
    public <R> R execute(String script, Class<? extends R> resultClass) {
        try {
            String key = keyGenerator.generateKey(null, script, resultClass.getName());
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
