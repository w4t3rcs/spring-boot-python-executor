package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractResolverCondition implements Condition {
    private static final String SPRING_PYTHON_RESOLVER_DECLARED_PROPERTY = "spring.python.resolver.declared";

    public boolean matchesByProperty(ConditionContext context, PythonResolverProperties.DeclaredResolver propertyValue) {
        Environment environment = context.getEnvironment();
        String[] property = environment.getProperty(SPRING_PYTHON_RESOLVER_DECLARED_PROPERTY, String[].class);
        return Arrays.stream(Objects.requireNonNull(property))
                .anyMatch(declared -> declared.equals(propertyValue.toString().toLowerCase()));
    }
}
