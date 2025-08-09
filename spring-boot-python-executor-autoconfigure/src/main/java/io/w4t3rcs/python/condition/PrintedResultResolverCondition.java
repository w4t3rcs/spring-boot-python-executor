package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.config.PythonResolverConfiguration;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * {@link AbstractResolverCondition} implementation that checks whether the
 * {@link PythonResolverProperties.DeclaredResolver#PRINTED_RESULT} resolver
 * is declared in the Spring environment property {@code spring.python.resolver.declared}.
 * <p>
 * This condition is used to enable components related to printed result processing
 * only when the {@code PRINTED_RESULT} resolver is explicitly declared.
 * </p>
 *
 * @see AbstractResolverCondition
 * @see PythonResolverProperties.DeclaredResolver#PRINTED_RESULT
 * @see PythonResolverConfiguration
 * @author w4t3rcs
 * @since 1.0.0
 */
@Getter(AccessLevel.PROTECTED)
public class PrintedResultResolverCondition extends AbstractResolverCondition {
    private final PythonResolverProperties.DeclaredResolver declaredResolver = PythonResolverProperties.DeclaredResolver.PRINTED_RESULT;
}
