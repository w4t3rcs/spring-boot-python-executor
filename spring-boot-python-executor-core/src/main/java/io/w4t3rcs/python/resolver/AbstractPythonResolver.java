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
 * Abstract class defining the contract for resolving Python scripts.
 * Implementations of this interface process Python scripts before execution,
 * applying transformations or resolving expressions within the script.
 */
public abstract class AbstractPythonResolver implements PythonResolver {
    /**
     * Constant for a Python JSON import statement.
     * This string is automatically added to Python scripts that need JSON functionality.
     */
    protected static final String IMPORT_JSON = "import json\n";
    protected static final int STRING_BUILDER_NO_VALUE_INDEX = -1;
    protected static final int STRING_BUILDER_START_INDEX = 0;
    public static final String IMPORT_VARIABLE_REGEX = "^\\s*(?:from\\s+([\\w.]+)\\s+import\\s+(.+)|import\\s+(.+))\\s*$";

    /**
     * Replaces fragments in a Python script that match a given regular expression pattern.
     * This method ensures the script includes the JSON import statement and applies
     * a custom transformation to matched fragments using the provided fragmentReplacer function.
     *
     * @param script The Python script to process
     * @param regex The regular expression pattern to match script fragments
     * @param positionFromStart The number of characters to skip from the start of the matched replacement
     * @param positionFromEnd The number of characters to skip from the end of the matched replacement
     * @param fragmentReplacer A function that transforms the matched expression string
     * @return The processed script with replaced fragments
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
     * Removes lines from a script that match a given regular expression pattern.
     * This method applies a custom consumer to each matched fragment before removing it from the script.
     *
     * @param script The script content to process
     * @param regex The regular expression pattern used to identify fragments
     * @param fragmentConsumer A consumer that processes each matched fragment with access to the {@link Matcher} and matched text
     * @return The modified script with all matched fragments removed
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
     * Inserts the line to a Python script if the script does not contain it
     *
     * @param script The Python script to process
     * @param insertable The line to be appended
     * @return The processed script with replaced fragments
     */
    protected StringBuilder insertUniqueLineToStart(StringBuilder script, String insertable) {
        if (script.indexOf(insertable) == STRING_BUILDER_NO_VALUE_INDEX) script.insert(STRING_BUILDER_START_INDEX, insertable + "\n");
        return script;
    }

    /**
     * Appends the line to a Python script with "\n"
     *
     * @param script The Python script to process
     * @param insertable The line to be appended
     * @return The processed script with replaced fragments
     */
    protected StringBuilder appendNextLine(StringBuilder script, String insertable) {
        script.append(insertable).append("\n");
        return script;
    }

    /**
     * Appends the line to a Python script with "\n"
     *
     * @param script The Python script to process
     * @param insertable The function that return a line that is going to be appended (useful for chained {@code append(...)} calls)
     * @return The processed script with replaced fragments
     */
    protected StringBuilder appendNextLine(StringBuilder script, Function<StringBuilder, StringBuilder> insertable) {
        insertable.apply(script).append("\n");
        return script;
    }

    /**
     * Finds the needed import variable names
     *
     * @param line The script line to process
     * @return The needed import names
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
}
