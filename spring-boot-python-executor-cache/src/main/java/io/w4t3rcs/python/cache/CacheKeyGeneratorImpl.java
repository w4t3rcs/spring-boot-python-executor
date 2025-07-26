package io.w4t3rcs.python.cache;

import io.w4t3rcs.python.exception.CacheKeyGenerationException;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.util.Base64;

@RequiredArgsConstructor
public class CacheKeyGeneratorImpl implements CacheKeyGenerator {
    private final PythonCacheProperties cacheProperties;

    @Override
    public String generateKey(String prefix, String body, String suffix) {
        try {
            var keyProperties = cacheProperties.key();
            MessageDigest digest = MessageDigest.getInstance(keyProperties.hashAlgorithm());
            byte[] hash = digest.digest(body.getBytes(keyProperties.charset()));
            String encodedHash = Base64.getEncoder().encodeToString(hash);
            String delimiter = keyProperties.delimiter();
            return (prefix == null ? "" : prefix + delimiter) + encodedHash + (suffix == null ? "" : delimiter + suffix);
        } catch (Exception e) {
            throw new CacheKeyGenerationException(e);
        }
    }
}
