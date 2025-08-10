package io.w4t3rcs.python.cache;

import io.w4t3rcs.python.cache.impl.HashCacheKeyGenerator;

/**
 * Interface for generating cache keys with optional prefix and suffix.
 * <p>
 * Provides default methods for generating cache keys with varying combinations
 * of prefix, body, and suffix parameters.
 * </p>
 * <p>
 * Implementations must provide the full {@link #generateKey(Object, String, Object)}
 * method which combines the prefix, body, and suffix into a cache key string.
 * </p>
 * <p>
 * The {@code body} parameter is typically the main part of the cache key and is
 * required for all key generation methods.
 * </p>
 *
 * <h3>Usage example:</h3>
 * <pre>{@code
 * CacheKeyGenerator generator = ...;
 * String key1 = generator.generateKey("bodyOnly");
 * String key2 = generator.generateKey("prefix", "body", 123);
 * }</pre>
 *
 * @see HashCacheKeyGenerator
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface CacheKeyGenerator {
    /**
     * Generates a cache key using only the {@code body}.
     *
     * @param body non-null string representing the main part of the key.
     * @return a non-null generated cache key string.
     */
    default String generateKey(String body) {
        return this.generateKey(null, body, null);
    }

    /**
     * Generates a cache key using {@code body} and a {@code suffix}.
     *
     * @param body non-null string representing the main part of the key.
     * @param suffix an optional suffix object to append to the key; may be null.
     * @return a non-null generated cache key string.
     */
    default String generateKey(String body, Object suffix) {
        return this.generateKey(null, body, suffix);
    }

    /**
     * Generates a cache key using a {@code prefix} and {@code body}.
     *
     * @param prefix an optional prefix object to prepend to the key; may be null.
     * @param body non-null string representing the main part of the key.
     * @return a non-null generated cache key string.
     */
    default String generateKey(Object prefix, String body) {
        return this.generateKey(prefix, body, null);
    }

    /**
     * Generates a cache key using a {@code prefix}, {@code body}, and {@code suffix}.
     * <p>
     * Implementations should concatenate or otherwise combine these parts to form
     * a unique cache key string.
     * </p>
     *
     * @param prefix an optional prefix object to prepend to the key; may be null.
     * @param body non-null string representing the main part of the key.
     * @param suffix an optional suffix object to append to the key; may be null.
     * @return a non-null generated cache key string.
     */
    String generateKey(Object prefix, String body, Object suffix);
}