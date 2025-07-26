package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import io.w4t3rcs.python.resolver.AbstractPythonResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resolver implementation that processes Python scripts for Py4J integration.
 * This resolver adds the necessary import statement to Python scripts to enable
 * communication between Java and Python using the Py4J bridge.
 * 
 * <p>The resolver can process both inline scripts and scripts loaded from files.</p>
 * 
 * <p>This resolver is conditionally enabled based on the {@code Py4JCondition}.</p>
 */
@Order(2)
@RequiredArgsConstructor
public class Py4JResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;

    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        StringBuilder resolvedScript = new StringBuilder(script);
        var py4JProperties = resolverProperties.py4j();
        List<String> importLines = new ArrayList<>();
        this.removeScriptLines(resolvedScript, resolverProperties.scriptImportsRegex(),
                (matcher, fragment) -> importLines.add(fragment));
        String gatewayObject = py4JProperties.gatewayObject();
        String gatewayProperties = String.join(",\n\t\t", py4JProperties.gatewayProperties());
        this.insertUniqueLineToStart(resolvedScript, gatewayObject.formatted(gatewayProperties));
        for (int i = importLines.size() - 1; i >= 0; i--) {
            this.insertUniqueLineToStart(resolvedScript, importLines.get(i));
        }
        this.insertUniqueLineToStart(resolvedScript, py4JProperties.importLine());
        return resolvedScript.toString();
    }
}
