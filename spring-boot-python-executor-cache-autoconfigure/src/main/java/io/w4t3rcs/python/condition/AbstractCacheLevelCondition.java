package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.config.PythonCacheAutoConfiguration;
import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.Objects;

/**
 * Abstract base class for Spring {@link Condition} implementations
 * that match based on the presence of a specific {@link PythonCacheProperties.PythonCacheLevel}
 * in the {@code spring.python.cache.levels} environment property.
 * <p>
 * This condition checks if the configured cache levels include the specific cache level
 * returned by the concrete subclass via {@link #getCacheLevel()}.
 * </p>
 *
 * @see PythonCacheProperties.PythonCacheLevel
 * @see PythonCacheAutoConfiguration
 * @see FileCacheLevelCondition
 * @see ResolverCacheLevelCondition
 * @see ExecutorCacheLevelCondition
 * @see ProcessorCacheLevelCondition
 * @author w4t3rcs
 * @since 1.0.0
 */
public abstract class AbstractCacheLevelCondition implements Condition {
    private static final String SPRING_PYTHON_CACHE_LEVELS_PROPERTY = "spring.python.cache.levels";

    /**
     * Evaluates this condition by checking if the cache level returned by
     * {@link #getCacheLevel()} is declared in the
     * {@code spring.python.cache.levels} property.
     *
     * @param context the condition context, providing access to environment and metadata (non-null)
     * @param metadata metadata of the annotated element (non-null)
     * @return {@code true} if the specified cache level is declared in the environment property,
     *         {@code false} otherwise
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return this.matchesByCacheLevel(context, this.getCacheLevel());
    }

    /**
     * Checks if the specified {@code propertyValue} cache level is declared in
     * the {@code spring.python.cache.levels} environment property.
     *
     * @param context the condition context, providing access to environment (non-null)
     * @param propertyValue the cache level to check for presence (non-null)
     * @return {@code true} if the cache level is declared, {@code false} otherwise
     * @throws NullPointerException if the property {@code spring.python.cache.levels} is missing or null
     */
    public boolean matchesByCacheLevel(ConditionContext context, PythonCacheProperties.PythonCacheLevel propertyValue) {
        Environment environment = context.getEnvironment();
        String[] property = environment.getProperty(SPRING_PYTHON_CACHE_LEVELS_PROPERTY, String[].class);
        return Arrays.stream(Objects.requireNonNull(property))
                .map(String::toLowerCase)
                .anyMatch(declared -> declared.equals(propertyValue.toString().toLowerCase()));
    }

    /**
     * Returns the {@link PythonCacheProperties.PythonCacheLevel} that this condition
     * checks for presence in the environment property.
     *
     * @return the cache level to check (non-null)
     */
    protected abstract PythonCacheProperties.PythonCacheLevel getCacheLevel();
}