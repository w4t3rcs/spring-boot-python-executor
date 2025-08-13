package io.w4t3rcs.python.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.dto.PythonExecutionResponse;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.TreeMap;

import static io.w4t3rcs.python.constant.TestConstants.*;
import static io.w4t3rcs.python.properties.PythonCacheProperties.NameProperties;

@ExtendWith(MockitoExtension.class)
class CachingPythonProcessorTests {
    private CachingPythonProcessor cachingPythonProcessor;
    @Mock
    private PythonProcessor pythonProcessor;
    @Mock
    private Cache cache;
    @Mock
    private CacheKeyGenerator keyGenerator;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PythonCacheProperties cacheProperties;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private NameProperties nameProperties;

    @BeforeEach
    void init() {
        Mockito.when(cacheProperties.name()).thenReturn(nameProperties);
        Mockito.when(nameProperties.processor()).thenReturn(CACHE_MANAGER_KEY);
        Mockito.when(cacheManager.getCache(CACHE_MANAGER_KEY)).thenReturn(cache);
        cachingPythonProcessor = new CachingPythonProcessor(cacheProperties, pythonProcessor, cacheManager, keyGenerator, objectMapper);
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1,
    })
    void testExistentKeyProcess(String script) {
        TreeMap<String, Object> sortedMap = new TreeMap<>(EMPTY_ARGUMENTS);

        Mockito.when(objectMapper.writeValueAsString(sortedMap)).thenReturn(EMPTY);
        Mockito.when(keyGenerator.generateKey(script, STRING_CLASS)).thenReturn(CACHE_KEY);
        Mockito.when((PythonExecutionResponse<String>) cache.get(CACHE_KEY, STRING_RESPONSE_CLASS)).thenReturn(OK_RESPONSE);

        String executed = cachingPythonProcessor.process(script, STRING_CLASS, EMPTY_ARGUMENTS).body();
        Assertions.assertEquals(OK, executed);
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1,
    })
    void testNonexistentKeyProcess(String script) {
        TreeMap<String, Object> sortedMap = new TreeMap<>(EMPTY_ARGUMENTS);

        Mockito.when(objectMapper.writeValueAsString(sortedMap)).thenReturn(EMPTY);
        Mockito.when(keyGenerator.generateKey(script, STRING_CLASS)).thenReturn(CACHE_KEY);
        Mockito.when((PythonExecutionResponse<String>) cache.get(CACHE_KEY, STRING_RESPONSE_CLASS)).thenReturn(null);
        Mockito.when((PythonExecutionResponse<String>) pythonProcessor.process(script, STRING_CLASS, EMPTY_ARGUMENTS)).thenReturn(OK_RESPONSE);
        Mockito.doNothing().when(cache).put(CACHE_KEY, OK_RESPONSE);

        String executed = cachingPythonProcessor.process(script, STRING_CLASS, EMPTY_ARGUMENTS).body();
        Assertions.assertEquals(OK, executed);
    }
}
