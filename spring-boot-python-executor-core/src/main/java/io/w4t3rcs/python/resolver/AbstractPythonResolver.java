package io.w4t3rcs.python.resolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base abstract implementation of {@link PythonResolver} for processing and transforming Python scripts
 * before execution.
 *
 * <p>This class provides utility methods for:
 * <ul>
 *   <li>Replacing script fragments matching a regular expression</li>
 *   <li>Removing lines based on a pattern</li>
 *   <li>Inserting or appending lines with duplicate-checking</li>
 *   <li>Extracting imported variable names from Python import statements</li>
 * </ul>
 *
 * @see PythonResolver
 * @see FragmentReplacer
 * @see FragmentReplacement
 * @see SpelythonResolver
 * @see Py4JResolver
 * @see RestrictedPythonResolver
 * @see ResultResolver
 * @see PrintedResultResolver
 * @author w4t3rcs
 * @since 1.0.0
 */
public abstract class AbstractPythonResolver implements PythonResolver {
    /**
     * Constant representing a Python JSON import statement.
     * <p>This line is automatically added to Python scripts that require JSON processing.</p>
     */
    protected static final String IMPORT_JSON = "import json\n";
    /**
     * Regular expression for matching Python import statements with optional {@code from ... import ...} syntax.
     * <p>Used by {@link #findImportVariables(String)} to detect imported variable names.</p>
     */
    protected static final String IMPORT_VARIABLE_REGEX = "^\\s*(?:from\\s+([\\w.]+)\\s+import\\s+(.+)|import\\s+(.+))\\s*$";
    /**
     * Constant indicating that a searched substring was not found in a {@link StringBuilder}.
     */
    protected static final int STRING_BUILDER_NO_VALUE_INDEX = -1;
    /**
     * Constant representing the starting index in a {@link StringBuilder}.
     */
    protected static final int STRING_BUILDER_START_INDEX = 0;

    /**
     * Replaces all fragments in the given Python script that match a specified regular expression.
     *
     * <p>The replacement is performed in reverse order of matches to preserve indexes. The matched
     * fragment is trimmed according to {@code positionFromStart} and {@code positionFromEnd} before
     * passing to the {@link FragmentReplacer}.
     *
     * <pre>{@code
     * StringBuilder script = new StringBuilder("print(42)");
     * StringBuilder updated = replaceScriptFragments(script, "\\d+", 0, 0, (matcher, fragment, body) -> body.append("100"));
     * // Result: print(100)
     * }</pre>
     *
     * @param script non-null Python script to process
     * @param regex non-null regular expression for matching script fragments
     * @param positionFromStart number of characters to skip from the start of the match, must be >= 0
     * @param positionFromEnd number of characters to skip from the end of the match, must be >= 0
     * @param fragmentReplacer non-null {@link FragmentReplacer} to transform each matched fragment
     * @return non-null {@link StringBuilder} instance with modifications applied
     */
    protected StringBuilder replaceScriptFragments(StringBuilder script, String regex, int positionFromStart, int positionFromEnd, FragmentReplacer fragmentReplacer) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(script.toString());
        List<FragmentReplacement> fragmentReplacements = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group();
            String expressionString = group.substring(positionFromStart, group.length() - positionFromEnd);
            StringBuilder expressionBody = new StringBuilder(expressionString);
            StringBuilder emptyBody = new StringBuilder();
            StringBuilder result = fragmentReplacer.replace(matcher, expressionBody, emptyBody);
            fragmentReplacements.add(new FragmentReplacement(result.toString(), matcher.start(), matcher.end()));
        }
        for (int i = fragmentReplacements.size() - 1; i >= 0; i--) {
            FragmentReplacement replacement = fragmentReplacements.get(i);
            script.replace(replacement.start(), replacement.end(), replacement.replacement());
        }
        return script;
    }

    /**
     * Removes all lines from the given Python script that match the specified pattern.
     *
     * <p>Before removal, each matched line is passed to the provided {@link BiConsumer} along with its {@link Matcher}.</p>
     *
     * @param script non-null script content to process
     * @param regex non-null pattern for identifying lines to remove
     * @param fragmentConsumer non-null consumer receiving each match before removal
     * @return non-null {@link StringBuilder} with all matched lines removed
     */
    protected StringBuilder removeScriptLines(StringBuilder script, String regex, BiConsumer<Matcher, String> fragmentConsumer) {
        Pattern pattern = Pattern.compile(regex);
        String scriptString = script.toString();
        AtomicInteger offset = new AtomicInteger();
        List<FragmentReplacement> fragmentReplacements = new ArrayList<>();
        scriptString.lines().forEach(line -> {
            int start = offset.get();
            int end = start + line.length();
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String group = matcher.group();
                fragmentConsumer.accept(matcher, group);
                fragmentReplacements.add(new FragmentReplacement("", start, end));
            }
            offset.set(end + 1);
        });
        for (int i = fragmentReplacements.size() - 1; i >= 0; i--) {
            FragmentReplacement replacement = fragmentReplacements.get(i);
            script.replace(replacement.start(), replacement.end(), replacement.replacement());
        }
        return script;
    }

    /**
     * Inserts a unique line at the start of the Python script if it is not already present.
     *
     * <p>The line is followed by a newline character.</p>
     *
     * @param script non-null Python script
     * @param insertable non-null line to insert
     * @return non-null {@link StringBuilder} with the line inserted if absent
     */
    protected StringBuilder insertUniqueLineToStart(StringBuilder script, String insertable) {
        if (!this.containsString(script, insertable)) script.insert(STRING_BUILDER_START_INDEX, insertable + "\n");
        return script;
    }

    /**
     * Appends a line followed by a newline to the script.
     *
     * @param script non-null Python script
     * @param insertable non-null line to append
     * @return non-null {@link StringBuilder} with the appended line
     */
    protected StringBuilder appendNextLine(StringBuilder script, String insertable) {
        script.append(insertable).append("\n");
        return script;
    }

    /**
     * Appends a line produced by the provided function followed by a newline to the script.
     *
     * <p>Useful for chained {@link StringBuilder#append(CharSequence)} operations.</p>
     *
     * @param script non-null Python script
     * @param insertable non-null function producing the appended content
     * @return non-null {@link StringBuilder} with the appended line
     */
    protected StringBuilder appendNextLine(StringBuilder script, Function<StringBuilder, StringBuilder> insertable) {
        insertable.apply(script).append("\n");
        return script;
    }

    /**
     * Extracts variable names from a Python import statement.
     *
     * <p>Supports both {@code import module} and {@code from module import name as alias} syntaxes.</p>
     *
     * @param line non-null Python script line to parse
     * @return non-null immutable {@link List} of imported variable names, possibly empty
     */
    protected List<String> findImportVariables(String line) {
        Pattern pattern = Pattern.compile(IMPORT_VARIABLE_REGEX);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) return Collections.emptyList();
        String fromImports = matcher.group(2);
        String importImports = matcher.group(3);
        String importsPart = fromImports != null ? fromImports : importImports;
        if (importsPart == null) return Collections.emptyList();
        String[] parts = importsPart.split(",\\s*");
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            String[] nameAlias = part.trim().split("\\s+as\\s+");
            if (nameAlias.length == 2) {
                result.add(nameAlias[1]);
            } else {
                result.add(nameAlias[0]);
            }
        }
        return result;
    }

    /**
     * Checks whether the given {@link StringBuilder} contains the specified string.
     *
     * @param resolvedScript non-null script to search
     * @param string non-null substring to search for
     * @return {@code true} if found, {@code false} otherwise
     */
    protected boolean containsString(StringBuilder resolvedScript, String string) {
        return resolvedScript.indexOf(string) != STRING_BUILDER_NO_VALUE_INDEX;
    }
}