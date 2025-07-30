package io.w4t3rcs.python.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.exception.PythonCacheException;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class CachingPythonResolverHolder implements PythonResolverHolder {
    private final PythonResolverHolder pythonResolverHolder;
    private final Cache cache;
    private final CacheKeyGenerator keyGenerator;
    private final ObjectMapper objectMapper;

    public CachingPythonResolverHolder(PythonCacheProperties cacheProperties, PythonResolverHolder pythonResolverHolder, CacheManager cacheManager, CacheKeyGenerator keyGenerator, ObjectMapper objectMapper) {
        this.pythonResolverHolder = pythonResolverHolder;
        this.cache = cacheManager.getCache(cacheProperties.name());
        this.keyGenerator = keyGenerator;
        this.objectMapper = objectMapper;
    }

    @Override
    public String resolveAll(String script, Map<String, Object> arguments) {
        try {
            Map<String, Object> sortedMap = new TreeMap<>(arguments);
            String argumentsJson = objectMapper.writeValueAsString(sortedMap);
            String body = script + argumentsJson;
            String key = keyGenerator.generateKey(null, body, null);
            String cachedResolvedScript = Objects.requireNonNull(cache).get(key, String.class);
            if (cachedResolvedScript != null) {
                return cachedResolvedScript;
            } else {
                String resolvedScript = pythonResolverHolder.resolveAll(script, arguments);
                cache.put(key, resolvedScript);
                return resolvedScript;
            }
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }

    @Override
    public List<PythonResolver> getResolvers() {
        return pythonResolverHolder.getResolvers();
    }
}
