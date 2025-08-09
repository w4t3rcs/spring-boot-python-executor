package io.w4t3rcs.python.resolver;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * {@link PythonResolver} implementation that processes result expressions in Python scripts.
 *
 * <p>This resolver searches for result expressions in the script using a configured regex pattern
 * and wraps each found expression with JSON serialization logic, assigning it to a configured
 * result variable.</p>
 *
 * <p>The processed script can then expose evaluated result data in a JSON-compatible form.</p>
 *
 * @see PythonResolver
 * @see AbstractPythonResolver
 * @see PythonResolverHolder
 * @see PythonResolverProperties.ResultProperties
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class ResultResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;

    /**
     * Resolves the script by finding and wrapping result expressions.
     *
     * @param script the original Python script content (non-null)
     * @param arguments unused map of variables, may be null
     * @return the processed script with result expressions replaced by JSON serialization assignments
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
                    .append(" = json.loads(json.dumps(")
                    .append(fragment)
                    .append("))"));
            return result;
        });
        return resolvedScript.toString();
    }
}
