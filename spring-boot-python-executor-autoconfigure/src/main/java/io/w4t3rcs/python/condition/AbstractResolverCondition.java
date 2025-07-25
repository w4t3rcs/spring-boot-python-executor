package io.w4t3rcs.python.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;

import java.util.Objects;

public abstract class AbstractResolverCondition implements Condition {
    private static final String SPRING_PYTHON_RESOLVER_DECLARED_PROPERTY = "spring.python.resolver.declared";

    public boolean matchesByProperty(ConditionContext context, PythonResolverProperties.DeclaredResolver propertyValue) {
        Environment environment = context.getEnvironment();
        String property = environment.getProperty(SPRING_PYTHON_RESOLVER_DECLARED_PROPERTY);
        return Objects.requireNonNull(property).contains(propertyValue.toString());
    }
}
