package io.w4t3rcs.python.resolver;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * This resolver adds print statements to the script to capture and format the results
 * of expressions, making them available for retrieval after script execution.
 */
@RequiredArgsConstructor
public class PrintedResultResolver extends AbstractPythonResolver {
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
        if (this.containsString(resolvedScript, resultProperties.appearance())) {
            this.appendNextLine(resolvedScript, builder -> builder.append("print('")
                    .append(resultProperties.appearance())
                    .append("' + json.dumps(")
                    .append(resultProperties.appearance())
                    .append("))"));
        }
        return resolvedScript.toString();
    }
}
