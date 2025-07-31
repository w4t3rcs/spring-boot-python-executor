package io.w4t3rcs.python.cache;

import io.w4t3rcs.python.cache.impl.CacheKeyGeneratorImpl;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static io.w4t3rcs.python.constant.TestConstants.OK;
import static io.w4t3rcs.python.properties.PythonCacheProperties.KeyProperties;

@ExtendWith(MockitoExtension.class)
class CacheKeyGeneratorTests {
    @InjectMocks
    private CacheKeyGeneratorImpl keyGenerator;
    @Mock
    private PythonCacheProperties cacheProperties;
    @Mock
    private KeyProperties keyProperties;

    @Test
    void testGenerateKey() {
        String prefix = "prefix";
        String suffix = "suffix";
        String hashedBody = "VlM5vE0z1ygXtYMCQRLrf1zfPl7vAlLW7BucmpThK7M=";

        Mockito.when(cacheProperties.key()).thenReturn(keyProperties);
        Mockito.when(keyProperties.hashAlgorithm()).thenReturn("SHA-256");
        Mockito.when(keyProperties.charset()).thenReturn("UTF-8");
        Mockito.when(keyProperties.delimiter()).thenReturn("_");

        String generated = keyGenerator.generateKey(prefix, OK, suffix);
        Assertions.assertTrue(generated.contains("_"));
        Assertions.assertTrue(generated.startsWith(prefix));
        Assertions.assertTrue(generated.endsWith(suffix));
        Assertions.assertTrue(generated.contains(hashedBody));
        Assertions.assertFalse(generated.contains(OK));
    }
}
