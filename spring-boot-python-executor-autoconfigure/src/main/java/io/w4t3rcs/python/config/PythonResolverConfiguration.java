package io.w4t3rcs.python.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.condition.*;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import io.w4t3rcs.python.resolver.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;
/**
 * Main configuration class for {@link PythonResolver} beans.
 * <p>
 * This configuration class defines and registers various {@link PythonResolver} implementations
 * as Spring beans, each conditionally created based on specific environment properties or
 * declared resolver conditions.
 * </p>
 * <p>
 * Each {@link PythonResolver} bean is assigned an order that determines the sequence of
 * resolver application when used collectively.
 * </p>
 * <p>
 * The configuration also provides a default {@link PythonResolverHolder} bean implementation
 * {@link BasicPythonResolverHolder} if no other {@link PythonResolverHolder} bean is present
 * in the Spring context.
 * </p>
 *
 * <pre>{@code
 * // Example of enabling the Spelython and Result resolvers via application.properties:
 * spring.python.resolver.declared=spelython, result
 *
 * // Example of enabling the Py4J resolver:
 * spring.python.resolver.declared=py4j
 * spring.python.py4j.enabled=true
 *
 * // Example usage of injected PythonResolverHolder in a service:
 * @Autowired
 * private PythonResolverHolder resolverHolder;
 *
 * public String executeScript(String script) {
 *     return resolverHolder.resolveAll(script, Map.of());
 * }
 * }</pre>
 *
 * @see PythonResolver
 * @see SpelythonResolver
 * @see Py4JResolver
 * @see RestrictedPythonResolver
 * @see ResultResolver
 * @see PrintedResultResolver
 * @see PythonResolverHolder
 * @see BasicPythonResolverHolder
 * @see SpelythonResolverCondition
 * @see Py4JResolverCondition
 * @see RestrictedPythonResolverCondition
 * @see ResultResolverCondition
 * @see PrintedResultResolverCondition
 * @author w4t3rcs
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(PythonResolverProperties.class)
public class PythonResolverConfiguration {
    /**
     * Order value for {@link SpelythonResolver} bean.
     */
    public static final int SPELYTHON_RESOLVER_ORDER = 0;
    /**
     * Order value for {@link Py4JResolver} bean.
     */
    public static final int PY4J_RESOLVER_ORDER = 50;
    /**
     * Order value for {@link RestrictedPythonResolver} bean.
     */
    public static final int RESTRICTED_PYTHON_RESOLVER_ORDER = 100;
    /**
     * Order value for {@link ResultResolver} bean.
     */
    public static final int RESULT_RESOLVER_ORDER = 150;
    /**
     * Order value for {@link PrintedResultResolver} bean.
     */
    public static final int PRINTED_RESULT_RESOLVER_ORDER = 200;

    /**
     * Creates a {@link SpelythonResolver} bean.
     * <p>
     * This bean is created only if {@link SpelythonResolverCondition} matches,
     * which requires {@code spring.python.resolver.declared} to contain "spelython".
     * </p>
     *
     * @param resolverProperties {@link PythonResolverProperties} bean, must not be null
     * @param applicationContext the Spring {@link ApplicationContext}, must not be null
     * @param objectMapper Jackson {@link ObjectMapper} bean, must not be null
     * @return configured {@link SpelythonResolver} instance, never null
     */
    @Bean
    @Order(SPELYTHON_RESOLVER_ORDER)
    @Conditional(SpelythonResolverCondition.class)
    public PythonResolver spelythonResolver(PythonResolverProperties resolverProperties, ApplicationContext applicationContext, ObjectMapper objectMapper) {
        return new SpelythonResolver(resolverProperties, applicationContext, objectMapper);
    }

    /**
     * Creates a {@link Py4JResolver} bean.
     * <p>
     * This bean is created only if {@link Py4JResolverCondition} matches and
     * the property {@code spring.python.py4j.enabled} is set to {@code true}.
     * </p>
     *
     * @param resolverProperties {@link PythonResolverProperties} bean, must not be null
     * @return configured {@link Py4JResolver} instance, never null
     */
    @Bean
    @Order(PY4J_RESOLVER_ORDER)
    @Conditional(Py4JResolverCondition.class)
    @ConditionalOnProperty(name = "spring.python.py4j.enabled", havingValue = "true")
    public PythonResolver py4JResolver(PythonResolverProperties resolverProperties) {
        return new Py4JResolver(resolverProperties);
    }

    /**
     * Creates a {@link RestrictedPythonResolver} bean.
     * <p>
     * This bean is created only if {@link RestrictedPythonResolverCondition} matches,
     * which requires {@code spring.python.resolver.declared} to contain "restricted_python".
     * </p>
     *
     * @param resolverProperties {@link PythonResolverProperties} bean, must not be null
     * @return configured {@link RestrictedPythonResolver} instance, never null
     */
    @Bean
    @Order(RESTRICTED_PYTHON_RESOLVER_ORDER)
    @Conditional(RestrictedPythonResolverCondition.class)
    public PythonResolver restrictedPythonResolver(PythonResolverProperties resolverProperties) {
        return new RestrictedPythonResolver(resolverProperties);
    }

    /**
     * Creates a {@link ResultResolver} bean.
     * <p>
     * This bean is created only if {@link ResultResolverCondition} matches,
     * which requires {@code spring.python.resolver.declared} to contain "result".
     * </p>
     *
     * @param resolverProperties {@link PythonResolverProperties} bean, must not be null
     * @return configured {@link ResultResolver} instance, never null
     */
    @Bean
    @Order(RESULT_RESOLVER_ORDER)
    @Conditional(ResultResolverCondition.class)
    public PythonResolver resultResolver(PythonResolverProperties resolverProperties) {
        return new ResultResolver(resolverProperties);
    }

    /**
     * Creates a {@link PrintedResultResolver} bean.
     * <p>
     * This bean is created only if {@link ResultResolverCondition} matches,
     * which requires {@code spring.python.resolver.declared} to contain "printed_result".
     * </p>
     *
     * @param resolverProperties {@link PythonResolverProperties} bean, must not be null
     * @return configured {@link PrintedResultResolver} instance, never null
     */
    @Bean
    @Order(PRINTED_RESULT_RESOLVER_ORDER)
    @Conditional(PrintedResultResolverCondition.class)
    public PythonResolver printedResultResolver(PythonResolverProperties resolverProperties) {
        return new PrintedResultResolver(resolverProperties);
    }

    /**
     * Creates the default {@link PythonResolverHolder} bean if none is defined.
     * <p>
     * This holder aggregates all available {@link PythonResolver} beans in the context.
     * </p>
     *
     * @param pythonResolvers list of all registered {@link PythonResolver} beans, never null but can be empty
     * @return a {@link BasicPythonResolverHolder} instance containing the given resolvers, never null
     */
    @Bean
    @ConditionalOnMissingBean(PythonResolverHolder.class)
    public PythonResolverHolder basicPythonResolverHolder(List<PythonResolver> pythonResolvers) {
        return new BasicPythonResolverHolder(pythonResolvers);
    }
}