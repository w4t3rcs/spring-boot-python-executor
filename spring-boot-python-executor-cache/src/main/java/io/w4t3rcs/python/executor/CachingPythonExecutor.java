package io.w4t3rcs.python.executor;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.exception.PythonCacheException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;

@RequiredArgsConstructor
public class CachingPythonExecutor implements PythonExecutor {
    private final PythonExecutor pythonExecutor;
    private final Cache cache;
    private final CacheKeyGenerator keyGenerator;

    @Override
    public <R> R execute(String script, Class<? extends R> resultClass) {
        try {
            String key = keyGenerator.generateKey(null, script, resultClass.getName());
            R cachedResult = cache.get(key, resultClass);
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
