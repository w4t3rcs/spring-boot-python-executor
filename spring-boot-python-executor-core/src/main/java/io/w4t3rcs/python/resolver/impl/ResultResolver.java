package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import io.w4t3rcs.python.resolver.AbstractPythonResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * Resolver implementation that processes result expressions in Python scripts.
 * This resolver adds print statements to the script to capture and format the results
 * of expressions, making them available for retrieval after script execution.
 * 
 * <p>The resolver can process both inline scripts and scripts loaded from files.</p>
 */
@Order(4)
@RequiredArgsConstructor
public class ResultResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;

    /**
     * Processes a script to find result expressions and wraps them in print statements.
     * 
     * <p>This method uses the configured regex pattern to find result expressions in the script
     * and wraps them in print statements with a specific appearance prefix to make them
     * identifiable in the output.</p>
     *
     * @param script The Python script content to process
     * @param arguments A map of variables that may be used during resolution, however, they are not used here.
     * @return The processed script with result expressions wrapped in print statements
     */
    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        StringBuilder resolvedScript = new StringBuilder(script);
        this.insertUniqueLineToStart(resolvedScript, AbstractPythonResolver.IMPORT_JSON);
        var resultProperties = resolverProperties.result();
        this.replaceScriptFragments(resolvedScript, resultProperties.regex(),
                resultProperties.positionFromStart(), resultProperties.positionFromEnd(),
                (matcher, fragment, result) -> {
            this.appendNextLine(result, builder -> builder.append(resultProperties.appearance())
                    .append(" = json.dumps(")
                    .append(fragment)
                    .append(")"));
            this.appendNextLine(result, builder -> builder.append("print('")
                    .append(resultProperties.appearance())
                    .append("' + ")
                    .append(resultProperties.appearance())
                    .append(")"));
            return result;
        });
        return resolvedScript.toString();
    }
}
