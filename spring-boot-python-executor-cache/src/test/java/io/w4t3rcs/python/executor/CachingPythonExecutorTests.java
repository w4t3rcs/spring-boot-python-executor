package io.w4t3rcs.python.executor;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.dto.PythonExecutionResponse;
import io.w4t3rcs.python.properties.PythonCacheProperties;
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

import static io.w4t3rcs.python.constant.TestConstants.*;
import static io.w4t3rcs.python.properties.PythonCacheProperties.NameProperties;

@ExtendWith(MockitoExtension.class)
class CachingPythonExecutorTests {
    private CachingPythonExecutor cachingPythonExecutor;
    @Mock
    private PythonExecutor pythonExecutor;
    @Mock
    private Cache cache;
    @Mock
    private CacheKeyGenerator keyGenerator;
    @Mock
    private PythonCacheProperties cacheProperties;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private NameProperties nameProperties;

    @BeforeEach
    void init() {
        Mockito.when(cacheProperties.name()).thenReturn(nameProperties);
        Mockito.when(nameProperties.executor()).thenReturn(CACHE_MANAGER_KEY);
        Mockito.when(cacheManager.getCache(CACHE_MANAGER_KEY)).thenReturn(cache);
        cachingPythonExecutor = new CachingPythonExecutor(cacheProperties, pythonExecutor, cacheManager, keyGenerator);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1,
    })
    void testExistentKeyExecute(String script) {
        Mockito.when(keyGenerator.generateKey(script, STRING_CLASS)).thenReturn(CACHE_KEY);
        Mockito.when((PythonExecutionResponse<String>) cache.get(CACHE_KEY, STRING_RESPONSE_CLASS)).thenReturn(OK_RESPONSE);

        String executed = cachingPythonExecutor.execute(script, STRING_CLASS).body();
        Assertions.assertEquals(OK, executed);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1,
    })
    void testNonexistentKeyExecute(String script) {
        Mockito.when(keyGenerator.generateKey(script, STRING_CLASS)).thenReturn(CACHE_KEY);
        Mockito.when((PythonExecutionResponse<String>) cache.get(CACHE_KEY, STRING_RESPONSE_CLASS)).thenReturn(null);
        Mockito.when((PythonExecutionResponse<String>) pythonExecutor.execute(script, STRING_CLASS)).thenReturn(OK_RESPONSE);
        Mockito.doNothing().when(cache).put(CACHE_KEY, OK_RESPONSE);

        String executed = cachingPythonExecutor.execute(script, STRING_CLASS).body();
        Assertions.assertEquals(OK, executed);
    }
}
