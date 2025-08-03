package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PROTECTED)
public class SpelythonResolverCondition extends AbstractResolverCondition {
    private final PythonResolverProperties.DeclaredResolver declaredResolver = PythonResolverProperties.DeclaredResolver.SPELYTHON;
}
