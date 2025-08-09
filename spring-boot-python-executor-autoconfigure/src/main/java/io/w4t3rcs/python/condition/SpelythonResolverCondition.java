package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.config.PythonResolverConfiguration;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * {@link AbstractResolverCondition} implementation that checks whether the
 * {@link PythonResolverProperties.DeclaredResolver#SPELYTHON} resolver
 * is declared in the Spring environment property {@code spring.python.resolver.declared}.
 * <p>
 * This condition activates components related to SpEL expression resolution in Python scripts
 * only if the {@code SPELYTHON} resolver is explicitly declared.
 * </p>
 *
 * @see AbstractResolverCondition
 * @see PythonResolverProperties.DeclaredResolver#SPELYTHON
 * @see PythonResolverConfiguration
 * @author w4t3rcs
 * @since 1.0.0
 */
@Getter(AccessLevel.PROTECTED)
public class SpelythonResolverCondition extends AbstractResolverCondition {
    private final PythonResolverProperties.DeclaredResolver declaredResolver = PythonResolverProperties.DeclaredResolver.SPELYTHON;
}
