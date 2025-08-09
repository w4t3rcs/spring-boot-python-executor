package io.w4t3rcs.python.resolver;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.RequiredArgsConstructor;

import java.util.Map;
/**
 * {@link PythonResolver} implementation that processes Python scripts to capture and format result expressions
 * by wrapping them with print statements.
 *
 * <p>This resolver identifies result expressions in the script based on a configured regex pattern
 * and then ensures these expressions are output during script execution in a structured format.
 * The print statements prepend a unique appearance prefix, allowing downstream processing or log parsing
 * systems to reliably detect and extract these results from the script output.</p>
 *
 * <p>This approach is useful in environments where the script execution environment only allows
 * capturing standard output streams, so explicit print statements become the way to communicate
 * back evaluation results.</p>
 *
 * @see PythonResolver
 * @see AbstractPythonResolver
 * @see PythonResolverHolder
 * @see PythonResolverProperties.ResultProperties
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class PrintedResultResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;

    /**
     * Resolves the Python script by wrapping configured result expressions with print statements.
     *
     * @param script the original Python script to process (non-null)
     * @param arguments unused map of variables, may be null or empty
     * @return the transformed script with print statements added for results
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
