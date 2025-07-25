package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.config.Py4JCondition;
import io.w4t3rcs.python.config.PythonResolverProperties;
import io.w4t3rcs.python.resolver.AbstractPythonResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * Resolver implementation that processes Python scripts for Py4J integration.
 * This resolver adds the necessary import statement to Python scripts to enable
 * communication between Java and Python using the Py4J bridge.
 * 
 * <p>The resolver can process both inline scripts and scripts loaded from files.</p>
 * 
 * <p>This resolver is conditionally enabled based on the {@link Py4JCondition}.</p>
 */
@Order(2)
@RequiredArgsConstructor
public class Py4JResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;

    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        StringBuilder resolvedScript = new StringBuilder(script);
        var py4JProperties = resolverProperties.py4j();
        this.insertUniqueLineToStart(resolvedScript, py4JProperties.gateway());
        this.insertUniqueLineToStart(resolvedScript, py4JProperties.importLine());
        return resolvedScript.toString();
    }
}
