package io.w4t3rcs.python.condition;

import io.w4t3rcs.python.config.PythonResolverConfiguration;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.Objects;

/**
 * Base {@link Condition} implementation that checks for the presence of a specific
 * declared Python resolver in the Spring environment configuration.
 * <p>
 * This condition verifies whether the {@code spring.python.resolver.declared} property
 * contains a given resolver identifier.
 * </p>
 * <p>
 * Implementations must specify the resolver to check by overriding {@link #getDeclaredResolver()}.
 * </p>
 * <p>
 * The property {@code spring.python.resolver.declared} is expected to be a list/array of strings.
 * If the property is missing or empty, the condition will not match.
 * </p>
 *
 * @see PythonResolverProperties.DeclaredResolver
 * @see PythonResolverConfiguration
 * @see SpelythonResolverCondition
 * @see Py4JResolverCondition
 * @see RestrictedPythonResolverCondition
 * @see ResultResolverCondition
 * @see PrintedResultResolverCondition
 * @author w4t3rcs
 * @since 1.0.0
 */
public abstract class AbstractResolverCondition implements Condition {
    private static final String SPRING_PYTHON_RESOLVER_DECLARED_PROPERTY = "spring.python.resolver.declared";

    /**
     * Checks whether the Spring environment contains the configured declared resolver.
     * Delegates the check to {@link #matchesByDeclaredResolver(ConditionContext, PythonResolverProperties.DeclaredResolver)}.
     *
     * @param context the condition context, must not be {@code null}
     * @param metadata metadata of the annotated component, ignored in this implementation
     * @return {@code true} if the declared resolver is present in the environment property, {@code false} otherwise
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return this.matchesByDeclaredResolver(context, this.getDeclaredResolver());
    }

    /**
     * Checks if the specified declared resolver is present in the
     * {@code spring.python.resolver.declared} property of the environment.
     * <p>
     * The property is read as a string array and matched case-insensitively against
     * the string representation of the {@code propertyValue} argument.
     * </p>
     * <p>
     * If the property is missing or empty, this method returns {@code false}.
     * </p>
     *
     * @param context the condition context providing access to environment, must not be {@code null}
     * @param propertyValue the declared resolver to check for, must not be {@code null}
     * @return {@code true} if the declared resolver is found in the property, {@code false} otherwise
     */
    protected boolean matchesByDeclaredResolver(ConditionContext context, PythonResolverProperties.DeclaredResolver propertyValue) {
        Environment environment = context.getEnvironment();
        String[] property = environment.getProperty(SPRING_PYTHON_RESOLVER_DECLARED_PROPERTY, String[].class);
        return Arrays.stream(Objects.requireNonNull(property))
                .map(String::toLowerCase)
                .anyMatch(declared -> declared.equals(propertyValue.toString().toLowerCase()));
    }

    /**
     * Returns the {@link PythonResolverProperties.DeclaredResolver} value
     * that this condition should match against.
     *
     * @return the declared resolver to check, never {@code null}
     */
    protected abstract PythonResolverProperties.DeclaredResolver getDeclaredResolver();
}
