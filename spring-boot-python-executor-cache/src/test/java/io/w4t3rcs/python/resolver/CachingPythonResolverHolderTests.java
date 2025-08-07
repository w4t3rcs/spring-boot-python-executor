package io.w4t3rcs.python.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.TreeMap;

import static io.w4t3rcs.python.constant.TestConstants.*;
import static io.w4t3rcs.python.properties.PythonCacheProperties.NameProperties;

@ExtendWith(MockitoExtension.class)
class CachingPythonResolverHolderTests {
    private CachingPythonResolverHolder cachingPythonResolverHolder;
    @Mock
    private PythonResolverHolder pythonResolverHolder;
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
    private PythonResolver pythonResolver;
    @Mock
    private NameProperties nameProperties;

    @BeforeEach
    void init() {
        Mockito.when(cacheProperties.name()).thenReturn(nameProperties);
        Mockito.when(nameProperties.resolver()).thenReturn(CACHE_MANAGER_KEY);
        Mockito.when(cacheManager.getCache(CACHE_MANAGER_KEY)).thenReturn(cache);
        cachingPythonResolverHolder = new CachingPythonResolverHolder(cacheProperties, pythonResolverHolder, cacheManager, keyGenerator, objectMapper);
    }

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1,
    })
    void testExistentKeyResolveAll(String script) {
        TreeMap<String, Object> sortedMap = new TreeMap<>(EMPTY_ARGUMENTS);

        Mockito.when(objectMapper.writeValueAsString(sortedMap)).thenReturn(EMPTY);
        Mockito.when(keyGenerator.generateKey(script)).thenReturn(CACHE_KEY);
        Mockito.when((String) cache.get(CACHE_KEY, STRING_CLASS)).thenReturn(OK);

        String executed = cachingPythonResolverHolder.resolveAll(script, EMPTY_ARGUMENTS);
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
    void testNonexistentKeyResolveAll(String script) {
        TreeMap<String, Object> sortedMap = new TreeMap<>(EMPTY_ARGUMENTS);

        Mockito.when(objectMapper.writeValueAsString(sortedMap)).thenReturn(EMPTY);
        Mockito.when(keyGenerator.generateKey(script)).thenReturn(CACHE_KEY);
        Mockito.when((String) cache.get(CACHE_KEY, STRING_CLASS)).thenReturn(null);
        Mockito.when(pythonResolverHolder.resolveAll(script, EMPTY_ARGUMENTS)).thenReturn(OK);
        Mockito.doNothing().when(cache).put(CACHE_KEY, OK);

        String executed = cachingPythonResolverHolder.resolveAll(script, EMPTY_ARGUMENTS);
        Assertions.assertEquals(OK, executed);
    }

    @Test
    void testGetResolvers() {
        List<PythonResolver> pythonResolvers = List.of(pythonResolver);

        Mockito.when(pythonResolverHolder.getResolvers()).thenReturn(pythonResolvers);

        Assertions.assertEquals(pythonResolvers, cachingPythonResolverHolder.getResolvers());
    }
}
