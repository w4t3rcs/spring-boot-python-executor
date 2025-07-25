package io.w4t3rcs.python.resolver;

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

    /**
     * Replaces fragments in a Python script that match a given regular expression pattern.
     * This method ensures the script includes the JSON import statement and applies
     * a custom transformation to matched fragments using the provided fragmentReplacer function.
     *
     * @param script The Python script to process
     * @param regex The regular expression pattern to match script fragments
     * @param positionFromStart The number of characters to skip from the start of the matched group
     * @param positionFromEnd The number of characters to skip from the end of the matched group
     * @param fragmentReplacer A function that transforms the matched expression string
     * @return The processed script with replaced fragments
     */
    protected StringBuilder replaceScriptFragments(StringBuilder script, String regex, int positionFromStart, int positionFromEnd, FragmentReplacer fragmentReplacer) {
        Pattern spelPattern = Pattern.compile(regex);
        Matcher matcher = spelPattern.matcher(script.toString());
        while (matcher.find()) {
            String group = matcher.group();
            String expressionString = group.substring(positionFromStart, group.length() - positionFromEnd);
            StringBuilder expressionBody = new StringBuilder(expressionString);
            StringBuilder result = fragmentReplacer.replace(matcher, expressionBody, new StringBuilder());
            script.replace(matcher.start(), matcher.end(), result.toString());
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
        if (script.indexOf(insertable) != STRING_BUILDER_NO_VALUE_INDEX) script.insert(STRING_BUILDER_START_INDEX, insertable + "\n");
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
        script.append(insertable.apply(script).toString()).append("\n");
        return script;
    }
}
