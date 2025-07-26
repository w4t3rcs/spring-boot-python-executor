package io.w4t3rcs.python.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.exception.PythonCacheException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.TreeMap;

@RequiredArgsConstructor
public class CachingPythonProcessor implements PythonProcessor{
    private final PythonProcessor pythonProcessor;
    private final Cache cache;
    private final CacheKeyGenerator keyGenerator;
    private final ObjectMapper objectMapper;

    @Override
    public <R> R process(String script, Class<? extends R> resultClass, Map<String, Object> arguments) {
        try {
            Map<String, Object> sortedMap = new TreeMap<>(arguments);
            String argumentsJson = objectMapper.writeValueAsString(sortedMap);
            String body = script + argumentsJson;
            String key = keyGenerator.generateKey(null, body, resultClass.getName());
            R cachedResult = cache.get(key, resultClass);
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
