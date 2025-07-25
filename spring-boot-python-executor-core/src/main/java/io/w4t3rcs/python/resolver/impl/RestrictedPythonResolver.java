package io.w4t3rcs.python.resolver.impl;

import io.w4t3rcs.python.config.PythonResolverProperties;
import io.w4t3rcs.python.resolver.AbstractPythonResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

import java.util.Map;
import java.util.Set;

/**
 * Resolver implementation that behaves like proxy using {@code RestrictedPython} Python lib in Python scripts.
 * This resolver adds necessary statements to the script to run code more safely using {@code RestrictedPython}
 *
 * <p>The resolver can process both inline scripts and scripts loaded from files.</p>
 */
@Order(3)
@RequiredArgsConstructor
public class RestrictedPythonResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;

    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        StringBuilder resolvedScript = new StringBuilder(script);
        var restrictedPythonProperties = resolverProperties.restrictedPython();
        var resultProperties = resolverProperties.result();
        resolvedScript.insert(STRING_BUILDER_START_INDEX, restrictedPythonProperties.codeVariableName() + " = \"\"\"\n");
        this.appendNextLine(resolvedScript, "\"\"\"");
        this.appendNextLine(resolvedScript, builder -> builder.append(restrictedPythonProperties.localVariablesName())
                .append(" = {}"));
        this.appendNextLine(resolvedScript, builder -> builder.append("byte_code = compile_restricted(")
                .append(restrictedPythonProperties.codeVariableName())
                .append(", '<inline>', 'exec')"));
        this.appendNextLine(resolvedScript, builder -> builder.append("exec(byte_code, safe_globals, ")
                .append(restrictedPythonProperties.localVariablesName())
                .append(")"));
        if (Set.of(resolverProperties.declared()).contains(PythonResolverProperties.DeclaredResolver.RESULT)) {
            this.replaceScriptFragments(resolvedScript, resultProperties.regex(),
                    resultProperties.positionFromStart(), resultProperties.positionFromEnd(),
                    (matcher, fragment, result) -> {
                result.append(restrictedPythonProperties.safeResultAppearance())
                        .append(" = json.dumps(")
                        .append(fragment)
                        .append(")\n");
                return result;
            });
            this.appendNextLine(resolvedScript, builder -> builder.append(resultProperties.appearance())
                    .append(" = ")
                    .append(restrictedPythonProperties.localVariablesName())
                    .append(".get('")
                    .append(restrictedPythonProperties.safeResultAppearance())
                    .append("')"));
            this.appendNextLine(resolvedScript, builder -> builder.append("print('")
                    .append(resultProperties.appearance())
                    .append("' + ")
                    .append(resultProperties.appearance())
                    .append(")"));
        }

        this.insertUniqueLineToStart(resolvedScript, restrictedPythonProperties.importLine());
        return resolvedScript.toString();
    }
}
