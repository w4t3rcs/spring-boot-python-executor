package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.config.PythonResolverConfiguration;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * {@link AbstractResolverCondition} implementation that verifies whether the
 * {@link PythonResolverProperties.DeclaredResolver#PY4J} resolver
 * is declared in the Spring environment property {@code spring.python.resolver.declared}.
 * <p>
 * This condition controls activation of components related to Py4J integration,
 * ensuring they are enabled only when the {@code PY4J} resolver is explicitly declared.
 * </p>
 *
 * @see AbstractResolverCondition
 * @see PythonResolverProperties.DeclaredResolver#PY4J
 * @see PythonResolverConfiguration
 * @author w4t3rcs
 * @since 1.0.0
 */
@Getter(AccessLevel.PROTECTED)
public class Py4JResolverCondition extends AbstractResolverCondition {
    private final PythonResolverProperties.DeclaredResolver declaredResolver = PythonResolverProperties.DeclaredResolver.PY4J;
}
