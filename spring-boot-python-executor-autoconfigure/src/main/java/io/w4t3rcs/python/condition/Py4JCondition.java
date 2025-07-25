package io.w4t3rcs.python.config;

import io.w4t3rcs.python.metadata.EnablePy4J;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.StandardMethodMetadata;

import java.util.Arrays;

/**
 * Condition implementation that determines whether Py4J integration should be enabled.
 * This condition checks both the application properties and the presence of the
 * {@link EnablePy4J} annotation to decide if Py4J-related beans should be created.
 * 
 * <p>The condition also verifies that the Py4J library is available on the classpath.</p>
 */
public class Py4JCondition implements Condition {
    private static final String PY4J_ENABLED_PROPERTY = "spring.python.py4j.enabled";
    private static final String GATEWAY_SERVER_CLASS_NAME = "py4j.GatewayServer";

    /**
     * Determines if the condition matches, which means Py4J integration should be enabled.
     * The condition matches if:
     * <ul>
     *   <li>The Py4J library is available on the classpath</li>
     *   <li>AND either:
     *     <ul>
     *       <li>The "spring.python.py4j.enabled" property is set to "true"</li>
     *       <li>OR the {@link EnablePy4J} annotation is present on a configuration class</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * @param context The condition context
     * @param metadata Metadata about the annotated type
     * @return true if Py4J integration should be enabled, false otherwise
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            Class.forName(GATEWAY_SERVER_CLASS_NAME, false, this.getClass().getClassLoader());
            Environment environment = context.getEnvironment();
            String property = environment.getProperty(PY4J_ENABLED_PROPERTY, "false");
            boolean isEnabledViaProperty = Boolean.parseBoolean(property);
            BeanDefinitionRegistry registry = context.getRegistry();
            boolean isEnabledViaAnnotation = Arrays.stream(registry.getBeanDefinitionNames())
                    .map(registry::getBeanDefinition)
                    .map(BeanDefinition::getSource)
                    .filter(StandardMethodMetadata.class::isInstance)
                    .map(StandardMethodMetadata.class::cast)
                    .anyMatch(methodMetadata -> methodMetadata.isAnnotated(EnablePy4J.class.getName()));
            return (isEnabledViaProperty || isEnabledViaAnnotation);
        } catch (Exception e) {
            return false;
        }
    }
}
