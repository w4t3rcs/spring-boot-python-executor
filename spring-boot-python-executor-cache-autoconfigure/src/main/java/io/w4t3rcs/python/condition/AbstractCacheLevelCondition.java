package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.properties.PythonCacheProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractCacheLevelCondition implements Condition {
    private static final String SPRING_PYTHON_CACHE_LEVELS_PROPERTY = "spring.python.cache.levels";

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return this.matchesByCacheLevel(context, this.getCacheLevel());
    }

    public boolean matchesByCacheLevel(ConditionContext context, PythonCacheProperties.PythonCacheLevel propertyValue) {
        Environment environment = context.getEnvironment();
        String[] property = environment.getProperty(SPRING_PYTHON_CACHE_LEVELS_PROPERTY, String[].class);
        return Arrays.stream(Objects.requireNonNull(property))
                .anyMatch(declared -> declared.equals(propertyValue.toString().toLowerCase()));
    }

    protected abstract PythonCacheProperties.PythonCacheLevel getCacheLevel();
}
