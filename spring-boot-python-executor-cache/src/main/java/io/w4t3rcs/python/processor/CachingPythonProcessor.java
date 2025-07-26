package io.w4t3rcs.python.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.exception.PythonCacheException;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@RequiredArgsConstructor
public class CachingPythonProcessor implements PythonProcessor {
    private final PythonCacheProperties cacheProperties;
    private final PythonProcessor pythonProcessor;
    private final CacheManager cacheManager;
    private final CacheKeyGenerator keyGenerator;
    private final ObjectMapper objectMapper;

    @Override
    public <R> R process(String script, Class<? extends R> resultClass, Map<String, Object> arguments) {
        try {
            Map<String, Object> sortedMap = new TreeMap<>(arguments);
            String argumentsJson = objectMapper.writeValueAsString(sortedMap);
            String body = script + argumentsJson;
            String key = keyGenerator.generateKey(null, body, resultClass.getName());
            Cache cache = cacheManager.getCache(cacheProperties.name());
            R cachedResult = Objects.requireNonNull(cache).get(key, resultClass);
            if (cachedResult != null) {
                return cachedResult;
            } else {
                R result = pythonProcessor.process(script, resultClass, arguments);
                cache.put(key, result);
                return result;
            }
        } catch (Exception e) {
            throw new PythonCacheException(e);
        }
    }
}
