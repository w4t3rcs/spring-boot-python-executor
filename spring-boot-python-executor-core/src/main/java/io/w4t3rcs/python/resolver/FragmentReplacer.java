package io.w4t3rcs.python.resolver;

import java.util.regex.Matcher;

/**
 * Functional interface defining a transformation for matched text fragments in a script.
 *
 * <p>Used in combination with methods such as
 * {@link AbstractPythonResolver#replaceScriptFragments(StringBuilder, String, int, int, FragmentReplacer)}
 * to replace parts of a script based on regular expression matches.</p>
 *
 * <pre>{@code
 * FragmentReplacer replacer = (matcher, fragment, body) -> {
 *     // Example: wrap matched text in quotes
 *     return body.append('"').append(fragment.toString()).append('"');
 * };
 * }</pre>
 *
 * @see FragmentReplacement
 * @see PythonResolver
 * @see AbstractPythonResolver
 * @author w4t3rcs
 * @since 1.0.0
 */
@FunctionalInterface
public interface FragmentReplacer {
    /**
     * Transforms a matched fragment into a replacement value.
     *
     * @param matcher non-null {@link Matcher} representing the match in the original text
     * @param fragment non-null {@link StringBuilder} containing the matched fragment to transform
     * @param result non-null {@link StringBuilder} that will hold the transformation body
     * @return non-null {@code body} containing the replacement text
     */
    StringBuilder replace(Matcher matcher, StringBuilder fragment, StringBuilder result);
}