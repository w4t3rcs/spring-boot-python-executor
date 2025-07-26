package io.w4t3rcs.python.cache;

public interface CacheKeyGenerator {
    String generateKey(String prefix, String body, String suffix);
}
