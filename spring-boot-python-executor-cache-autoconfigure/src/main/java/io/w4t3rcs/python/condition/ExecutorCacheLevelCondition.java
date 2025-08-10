package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.config.PythonCacheAutoConfiguration;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * {@link AbstractCacheLevelCondition} implementation that matches when
 * the {@code spring.python.cache.levels} property contains the cache level {@link PythonCacheProperties.PythonCacheLevel#EXECUTOR}.
 * <p>
 * This condition is used to enable caching features related to executor-level caching.
 * </p>
 * <p>
 * The cache level is fixed to {@link PythonCacheProperties.PythonCacheLevel#EXECUTOR}.
 * </p>
 *
 * @see AbstractCacheLevelCondition
 * @see PythonCacheProperties.PythonCacheLevel#EXECUTOR
 * @see PythonCacheAutoConfiguration
 * @author w4t3rcs
 * @since 1.0.0
 */
@Getter(AccessLevel.PROTECTED)
public class ExecutorCacheLevelCondition extends AbstractCacheLevelCondition {
    private final PythonCacheProperties.PythonCacheLevel cacheLevel = PythonCacheProperties.PythonCacheLevel.EXECUTOR;
}