package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class ResultResolverCondition extends AbstractResolverCondition {
    private static final PythonResolverProperties.DeclaredResolver PROPERTY_VALUE = PythonResolverProperties.DeclaredResolver.RESULT;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return this.matchesByProperty(context, PROPERTY_VALUE);
    }
}
