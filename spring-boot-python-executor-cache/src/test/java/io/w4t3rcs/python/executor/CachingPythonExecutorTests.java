package io.w4t3rcs.python.executor;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
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

    @BeforeEach
    void init() {
        Mockito.when(cacheProperties.name()).thenReturn(CACHE_MANAGER_KEY);
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
        Mockito.when(keyGenerator.generateKey(null, script, STRING_CLASS.getName())).thenReturn(CACHE_KEY);
        Mockito.when((String) cache.get(CACHE_KEY, STRING_CLASS)).thenReturn(OK);

        String executed = cachingPythonExecutor.execute(script, STRING_CLASS);
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
        Mockito.when(keyGenerator.generateKey(null, script, STRING_CLASS.getName())).thenReturn(CACHE_KEY);
        Mockito.when((String) cache.get(CACHE_KEY, STRING_CLASS)).thenReturn(null);
        Mockito.when((String) pythonExecutor.execute(script, STRING_CLASS)).thenReturn(OK);
        Mockito.doNothing().when(cache).put(CACHE_KEY, OK);

        String executed = cachingPythonExecutor.execute(script, STRING_CLASS);
        Assertions.assertEquals(OK, executed);
    }
}
