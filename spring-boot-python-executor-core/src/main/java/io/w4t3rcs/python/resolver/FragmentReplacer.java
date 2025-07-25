package io.w4t3rcs.python.resolver;

import java.util.regex.Matcher;

@FunctionalInterface
public interface FragmentReplacer {
    StringBuilder replace(Matcher matcher, StringBuilder fragment, StringBuilder result);
}
