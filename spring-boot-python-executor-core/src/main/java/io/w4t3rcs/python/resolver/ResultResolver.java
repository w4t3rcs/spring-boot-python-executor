package io.w4t3rcs.python.resolver;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Resolver implementation that processes result expressions in Python scripts.
 */
@RequiredArgsConstructor
public class ResultResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;

    /**
     * Processes a script to find result expressions
     * This method uses the configured regex pattern to find result expressions in the script
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
                    .append(" = json.loads(json.dumps(")
                    .append(fragment)
                    .append("))"));
            return result;
        });
        return resolvedScript.toString();
    }
}
