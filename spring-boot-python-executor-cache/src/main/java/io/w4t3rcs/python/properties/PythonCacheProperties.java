package io.w4t3rcs.python.properties;

import io.w4t3rcs.python.cache.impl.HashCacheKeyGenerator;
import io.w4t3rcs.python.executor.CachingPythonExecutor;
import io.w4t3rcs.python.file.CachingPythonFileHandler;
import io.w4t3rcs.python.processor.CachingPythonProcessor;
import io.w4t3rcs.python.resolver.CachingPythonResolverHolder;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Python caching functionality.
 * <p>
 * Maps properties under the prefix {@code spring.python.cache} to configure
 * caching behavior including enabling/disabling cache, cache levels, cache
 * names for different cache types, and key generation properties.
 * </p>
 *
 * <p>Example configuration in application.yml:</p>
 * <pre>{@code
 * spring:
 *   python:
 *     cache:
 *       enabled: true
 *       levels: file, processor
 *       name:
 *         file-paths: filePathsCache
 *         file-bodies: fileBodiesCache
 *         resolver: pythonResolverCache
 *         executor: pythonExecutorCache
 *         processor: pythonProcessorCache
 *       key:
 *         hashAlgorithm: SHA-256
 *         charset: UTF-8
 *         delimiter: _
 * }</pre>
 *
 * @see HashCacheKeyGenerator
 * @see CachingPythonFileHandler
 * @see CachingPythonResolverHolder
 * @see CachingPythonExecutor
 * @see CachingPythonProcessor
 * @author w4t3rcs
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "spring.python.cache")
public record PythonCacheProperties(boolean enabled, PythonCacheLevel[] levels, NameProperties name, KeyProperties key) {
    /**
     * Enumeration of available caching levels.
     * <p>
     * Determines which parts of Python processing flow are cached.
     * </p>
     */
    public enum PythonCacheLevel {
        FILE, RESOLVER, EXECUTOR, PROCESSOR
    }

    /**
     * Cache names for different cache segments.
     * <p>
     * Used to resolve cache instances by name in cache manager.
     * </p>
     *
     * @param filePaths cache name for cached file paths
     * @param fileBodies cache name for cached file bodies
     * @param resolver cache name for cached resolver results
     * @param executor cache name for cached executor results
     * @param processor cache name for cached processor results
     */
    public record NameProperties(String filePaths, String fileBodies, String resolver, String executor, String processor) {
    }

    /**
     * Properties related to cache key generation.
     * <p>
     * Defines hash algorithm, charset, and delimiter used to generate cache keys.
     * </p>
     *
     * @param hashAlgorithm name of the hashing algorithm (e.g. "SHA-256")
     * @param charset charset name used to encode strings before hashing (e.g. "UTF-8")
     * @param delimiter delimiter string used to separate key parts
     */
    public record KeyProperties(String hashAlgorithm, String charset, String delimiter) {
    }
}
