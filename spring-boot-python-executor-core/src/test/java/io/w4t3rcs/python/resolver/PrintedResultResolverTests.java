package io.w4t3rcs.python.resolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static io.w4t3rcs.python.constant.TestConstants.*;

class PrintedResultResolverTests {
    @ParameterizedTest
    @ValueSource(strings = {PRINTED_RESULT_SCRIPT_0, PRINTED_RESULT_SCRIPT_1, PRINTED_RESULT_SCRIPT_2, PRINTED_RESULT_SCRIPT_3})
    void testResolve(String script) {
        String resolved = PRINTED_RESULT_RESOLVER.resolve(script, Map.of());
        System.out.println(resolved);
        Assertions.assertTrue(resolved.contains(RESULT_PROPERTIES.appearance()));
        Assertions.assertTrue(resolved.contains("print('" + RESULT_PROPERTIES.appearance() + "' + json.dumps(" + RESULT_PROPERTIES.appearance() + "))"));
    }
}
