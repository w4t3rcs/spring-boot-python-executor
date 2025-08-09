package io.w4t3rcs.python.resolver;

import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link PythonResolver} implementation that processes Python scripts for secure execution
 * using the {@code RestrictedPython} library.
 *
 * <p>This resolver acts as a proxy, injecting necessary setup code to enable safe execution of Python code with RestrictedPython.
 * It performs the following operations:
 * <ul>
 *   <li>Removes existing import lines matching a configured regex and collects import names.</li>
 *   <li>Wraps the original script code in a string variable for compilation.</li>
 *   <li>Initializes local variables container for execution results.</li>
 *   <li>Compiles the wrapped script with RestrictedPython's compile_restricted method.</li>
 *   <li>Executes the compiled code in a safe globals context augmented with collected imports.</li>
 *   <li>Replaces configured result fragments with JSON serialization code if enabled.</li>
 *   <li>Inserts necessary import statements and setup for safe globals and optional print support.</li>
 * </ul>
 *
 * @see PythonResolver
 * @see AbstractPythonResolver
 * @see PythonResolverHolder
 * @see PythonResolverProperties.RestrictedPythonProperties
 * @see <a href="https://github.com/zopefoundation/RestrictedPython">RestrictedPython</a>
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class RestrictedPythonResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;

    /**
     * Resolves the given Python script by injecting RestrictedPython setup code to
     * enable secure execution.
     *
     * @param script the original Python script content (non-null)
     * @param arguments unused map of variables for script execution context, may be null
     * @return the transformed Python script ready for execution with RestrictedPython
     */
    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        StringBuilder resolvedScript = new StringBuilder(script);
        var restrictedPythonProperties = resolverProperties.restrictedPython();
        var resultProperties = resolverProperties.result();
        List<String> importLines = new ArrayList<>();
        List<String> importNames = new ArrayList<>();
        this.removeScriptLines(resolvedScript, resolverProperties.scriptImportsRegex(),
                (matcher, fragment) -> {
            importLines.add(fragment);
            List<String> currentImportNames = this.findImportVariables(fragment);
            importNames.addAll(currentImportNames);
        });
        resolvedScript.insert(STRING_BUILDER_START_INDEX, restrictedPythonProperties.codeVariableName() + " = \"\"\"\n");
        this.appendNextLine(resolvedScript, "\n\"\"\"");
        this.appendNextLine(resolvedScript, builder -> builder.append(restrictedPythonProperties.localVariablesName())
                .append(" = {}"));
        this.appendNextLine(resolvedScript, builder -> builder.append("restricted_byte_code = compile_restricted(")
                .append(restrictedPythonProperties.codeVariableName())
                .append(", '<inline>', 'exec')"));
        this.appendNextLine(resolvedScript, builder -> builder.append("exec(restricted_byte_code, safe_globals_with_imports, ")
                .append(restrictedPythonProperties.localVariablesName())
                .append(")"));
        if (Set.of(resolverProperties.declared()).contains(PythonResolverProperties.DeclaredResolver.RESULT)) {
            this.replaceScriptFragments(resolvedScript, resultProperties.regex(),
                    resultProperties.positionFromStart(), resultProperties.positionFromEnd(),
                    (matcher, fragment, result) -> {
                result.append(restrictedPythonProperties.safeResultAppearance())
                        .append(" = json.loads(json.dumps(")
                        .append(fragment)
                        .append("))\n");
                return result;
            });
            this.appendNextLine(resolvedScript, builder -> builder.append(resultProperties.appearance())
                    .append(" = ")
                    .append(restrictedPythonProperties.localVariablesName())
                    .append(".get('")
                    .append(restrictedPythonProperties.safeResultAppearance())
                    .append("', '')"));
        }
        for (int i = importNames.size() - 1; i >= 0; i--) {
            String importName = importNames.get(i);
            this.insertUniqueLineToStart(resolvedScript, "safe_globals_with_imports['" + importName + "'] = " + importName);
        }
        boolean printEnabled = restrictedPythonProperties.printEnabled();
        if (printEnabled) {
            this.insertUniqueLineToStart(resolvedScript, "safe_globals_with_imports['_getattr_'] = _getattr_");
            this.insertUniqueLineToStart(resolvedScript, "safe_globals_with_imports['_print_'] = _print_");
        }
        this.insertUniqueLineToStart(resolvedScript, "safe_globals_with_imports = dict(safe_globals)");
        if (printEnabled) {
            this.insertUniqueLineToStart(resolvedScript, "_getattr_ = getattr");
            this.insertUniqueLineToStart(resolvedScript, "_print_ = PrintCollector");
            this.insertUniqueLineToStart(resolvedScript, "from RestrictedPython.PrintCollector import PrintCollector");
        }
        for (int i = importLines.size() - 1; i >= 0; i--) {
            this.insertUniqueLineToStart(resolvedScript, importLines.get(i));
        }
        this.insertUniqueLineToStart(resolvedScript, AbstractPythonResolver.IMPORT_JSON);
        this.insertUniqueLineToStart(resolvedScript, restrictedPythonProperties.importLine());
        return resolvedScript.toString();
    }
}
