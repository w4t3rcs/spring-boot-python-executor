package io.w4t3rcs.python.cache.impl;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.exception.CacheKeyGenerationException;
import io.w4t3rcs.python.executor.CachingPythonExecutor;
import io.w4t3rcs.python.file.CachingPythonFileHandler;
import io.w4t3rcs.python.processor.CachingPythonProcessor;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import io.w4t3rcs.python.resolver.CachingPythonResolverHolder;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * {@link CacheKeyGenerator} implementation that generates cache keys by hashing
 * the {@code body} string using a configurable hashing algorithm and encoding the body in Base64.
 * <p>
 * The generated key format is: {@code [prefix][delimiter][hashedBody][delimiter][suffix]},
 * where {@code prefix} and {@code suffix} are optional and {@code delimiter} is configurable.
 * </p>
 * <p>
 * The hashing algorithm, charset, and delimiter are configured via
 * {@link PythonCacheProperties.KeyProperties}.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * PythonCacheProperties cacheProperties = ...; // configured properties
 * CacheKeyGenerator generator = new HashCacheKeyGenerator(cacheProperties);
 * String key = generator.generateKey("prefix", "myCacheKeyBody", "suffix");
 * }</pre>
 *
 * @see CacheKeyGenerator
 * @see PythonCacheProperties.KeyProperties
 * @see CachingPythonFileHandler
 * @see CachingPythonResolverHolder
 * @see CachingPythonExecutor
 * @see CachingPythonProcessor
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class HashCacheKeyGenerator implements CacheKeyGenerator {
    /**
     * Cache properties providing key generation configuration.
     * Must be non-null.
     */
    private final PythonCacheProperties cacheProperties;

    /**
     * Generates a cache key by hashing the {@code body} string with the configured
     * hash algorithm and encoding the body in Base64.
     * <p>
     * The resulting key consists of the optional {@code prefix}, the Base64-encoded
     * hash of the {@code body}, and the optional {@code suffix}, joined by the configured
     * delimiter string.
     * </p>
     * <p>
     * If {@code prefix} or {@code suffix} is null, they are omitted without extra delimiters.
     * </p>
     * <p>
     * This method throws {@link CacheKeyGenerationException} wrapping any checked exceptions
     * thrown during hashing.
     * </p>
     *
     * @param prefix optional prefix for the key, may be null.
     * @param body non-null main string to hash and encode, must not be null.
     * @param suffix optional suffix for the key, may be null.
     * @return non-null generated cache key string.
     * @throws CacheKeyGenerationException if hashing fails due to unsupported algorithm or encoding.
     */
    @Override
    public String generateKey(Object prefix, String body, Object suffix) {
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