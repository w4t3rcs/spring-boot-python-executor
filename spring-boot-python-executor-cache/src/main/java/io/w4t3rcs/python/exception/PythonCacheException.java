package io.w4t3rcs.python.exception;

import io.w4t3rcs.python.executor.CachingPythonExecutor;
import io.w4t3rcs.python.file.CachingPythonFileHandler;
import io.w4t3rcs.python.processor.CachingPythonProcessor;
import io.w4t3rcs.python.resolver.CachingPythonResolverHolder;

/**
 * Runtime exception indicating errors related to Python cache operations.
 * <p>
 * This exception wraps any underlying exceptions encountered during cache
 * handling in the Python integration context.
 * </p>
 *
 * @see CachingPythonFileHandler
 * @see CachingPythonResolverHolder
 * @see CachingPythonExecutor
 * @see CachingPythonProcessor
 * @author w4t3rcs
 * @since 1.0.0
 */
public class PythonCacheException extends RuntimeException {
    /**
     * Constructs a new {@code PythonCacheException} with the specified cause.
     *
     * @param cause the underlying cause of this exception; must not be null.
     */
    public PythonCacheException(Throwable cause) {
        super(cause);
    }
}