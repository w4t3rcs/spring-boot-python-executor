package io.w4t3rcs.python.file;

import io.w4t3rcs.python.properties.PythonCacheProperties;
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

import java.nio.file.Path;

import static io.w4t3rcs.python.constant.TestConstants.*;
import static io.w4t3rcs.python.properties.PythonCacheProperties.NameProperties;

@ExtendWith(MockitoExtension.class)
class CachingPythonFileHandlerTests {
    private CachingPythonFileHandler cachingPythonFileHandler;
    @Mock
    private PythonFileHandler pythonFileHandler;
    @Mock
    private Cache scriptBodyCache;
    @Mock
    private Cache pathCache;
    @Mock
    private PythonCacheProperties cacheProperties;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private NameProperties nameProperties;

    @BeforeEach
    void init() {
        Mockito.when(cacheProperties.name()).thenReturn(nameProperties);
        Mockito.when(nameProperties.fileBodies()).thenReturn(CACHE_MANAGER_KEY + "0");
        Mockito.when(nameProperties.filePaths()).thenReturn(CACHE_MANAGER_KEY + "1");
        Mockito.when(cacheManager.getCache(CACHE_MANAGER_KEY + "0")).thenReturn(scriptBodyCache);
        Mockito.when(cacheManager.getCache(CACHE_MANAGER_KEY + "1")).thenReturn(pathCache);
        cachingPythonFileHandler = new CachingPythonFileHandler(cacheProperties, pythonFileHandler, cacheManager);
    }

    @Test
    void testCheckPythonFile() {
        Mockito.when(pythonFileHandler.isPythonFile(FILE_SCRIPT)).thenReturn(true);

        Assertions.assertTrue(cachingPythonFileHandler.isPythonFile(FILE_SCRIPT));
    }

    @Test
    void testExistentKeyReadScriptBodyFromFile() {
        Mockito.when((Path) pathCache.get(FILE_SCRIPT, PATH_CLASS)).thenReturn(FILE_PATH);
        Mockito.when((String) scriptBodyCache.get(FILE_PATH, STRING_CLASS)).thenReturn(OK);
        
        String body = cachingPythonFileHandler.readScriptBodyFromFile(FILE_SCRIPT);
        Assertions.assertEquals(OK, body);
    }

    @Test
    void testNonexistentKeyReadScriptBodyFromFile() {
        Mockito.when((Path) pathCache.get(FILE_SCRIPT, PATH_CLASS)).thenReturn(null);
        Mockito.when(pythonFileHandler.getScriptPath(FILE_SCRIPT)).thenReturn(FILE_PATH);
        Mockito.doNothing().when(pathCache).put(FILE_SCRIPT, FILE_PATH);
        Mockito.when((String) scriptBodyCache.get(FILE_PATH, STRING_CLASS)).thenReturn(null);
        Mockito.when(pythonFileHandler.readScriptBodyFromFile(FILE_PATH)).thenReturn(OK);
        Mockito.doNothing().when(scriptBodyCache).put(FILE_PATH, OK);

        String body = cachingPythonFileHandler.readScriptBodyFromFile(FILE_SCRIPT);
        Assertions.assertEquals(OK, body);
    }

    @Test
    void testExistentKeyReadScriptBodyFromFileWithMapper() {
        Mockito.when((Path) pathCache.get(FILE_SCRIPT, PATH_CLASS)).thenReturn(FILE_PATH);
        Mockito.when((String) scriptBodyCache.get(FILE_PATH, STRING_CLASS)).thenReturn(OK);

        String body = cachingPythonFileHandler.readScriptBodyFromFile(FILE_SCRIPT, String::toLowerCase);
        Assertions.assertEquals(OK.toLowerCase(), body);
    }

    @Test
    void testNonexistentKeyReadScriptBodyWithMapper() {
        Mockito.when((Path) pathCache.get(FILE_SCRIPT, PATH_CLASS)).thenReturn(null);
        Mockito.when(pythonFileHandler.getScriptPath(FILE_SCRIPT)).thenReturn(FILE_PATH);
        Mockito.doNothing().when(pathCache).put(FILE_SCRIPT, FILE_PATH);
        Mockito.when((String) scriptBodyCache.get(FILE_PATH, STRING_CLASS)).thenReturn(null);
        Mockito.when(pythonFileHandler.readScriptBodyFromFile(FILE_PATH)).thenReturn(OK);
        Mockito.doNothing().when(scriptBodyCache).put(FILE_PATH, OK);

        String body = cachingPythonFileHandler.readScriptBodyFromFile(FILE_SCRIPT, String::toLowerCase);
        Assertions.assertEquals(OK.toLowerCase(), body);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1,
    })
    void testWriteScriptBodyToFile(String script) {
        Mockito.when((Path) pathCache.get(FILE_SCRIPT, PATH_CLASS)).thenReturn(FILE_PATH);
        Mockito.when((String) scriptBodyCache.get(FILE_PATH, STRING_CLASS)).thenReturn(OK);
        Mockito.when(scriptBodyCache.evictIfPresent(FILE_PATH)).thenReturn(false);
        Mockito.doNothing().when(pythonFileHandler).writeScriptBodyToFile(FILE_PATH, script);

        cachingPythonFileHandler.writeScriptBodyToFile(FILE_SCRIPT, script);
        String body = cachingPythonFileHandler.readScriptBodyFromFile(FILE_SCRIPT);
        Assertions.assertEquals(OK, body);
    }
}
