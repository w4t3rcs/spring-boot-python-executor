package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.cache.CacheKeyGenerator;
import io.w4t3rcs.python.cache.impl.HashCacheKeyGenerator;

/**
 * Runtime exception thrown when cache key generation fails.
 * <p>
 * This exception wraps any underlying checked exceptions encountered during
 * the cache key generation process, such as cryptographic or encoding errors.
 * </p>
 * <p>
 * Use this exception to indicate unrecoverable errors in {@link CacheKeyGenerator} implementations.
 * </p>
 *
 * @see CacheKeyGenerator
 * @see HashCacheKeyGenerator
 * @author w4t3rcs
 * @since 1.0.0
 */
public class CacheKeyGenerationException extends RuntimeException {
    /**
     * Constructs a new {@code CacheKeyGenerationException} with the specified cause.
     *
     * @param cause the underlying cause of the exception; must not be null.
     */
    public CacheKeyGenerationException(Throwable cause) {
        super(cause);
    }
}