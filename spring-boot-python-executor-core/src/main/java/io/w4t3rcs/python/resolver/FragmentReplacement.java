package io.w4t3rcs.python.resolver;

/**
 * Immutable data record representing a text fragment replacement operation.
 *
 * <p>Holds:
 * <ul>
 *   <li>The replacement string</li>
 *   <li>The start index (inclusive) in the target text</li>
 *   <li>The end index (exclusive) in the target text</li>
 * </ul>
 *
 * @param replacement non-null string to insert into the target text
 * @param start zero-based inclusive start index in the target text, must be >= 0
 * @param end zero-based exclusive end index in the target text, must be >= start
 * @see FragmentReplacer
 * @see PythonResolver
 * @see AbstractPythonResolver
 * @see java.lang.StringBuilder#replace(int, int, String)
 * @author w4t3rcs
 * @since 1.0.0
 */
public record FragmentReplacement(String replacement, int start, int end) {
}
