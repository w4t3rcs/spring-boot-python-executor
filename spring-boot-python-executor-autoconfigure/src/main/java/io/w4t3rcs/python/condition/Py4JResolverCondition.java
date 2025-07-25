package io.w4t3rcs.python.config;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class Py4JResolverCondition extends AbstractResolverCondition {
    private static final PythonResolverProperties.DeclaredResolver PROPERTY_VALUE = PythonResolverProperties.DeclaredResolver.PY4J;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return this.matchesByProperty(context, PROPERTY_VALUE);
    }
}
