package io.w4t3rcs.python.cache;

public interface CacheKeyGenerator {
    default String generateKey(String body) {
        return this.generateKey(null, body, null);
    }

    default String generateKey(String body, Object suffix) {
        return this.generateKey(null, body, suffix);
    }

    default String generateKey(Object prefix, String body) {
        return this.generateKey(prefix, body, null);
    }

    String generateKey(Object prefix, String body, Object suffix);
}
