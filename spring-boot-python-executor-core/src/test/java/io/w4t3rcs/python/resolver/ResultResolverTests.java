package io.w4t3rcs.python.resolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static io.w4t3rcs.python.constant.TestConstants.*;

class ResultResolverTests {
    @ParameterizedTest
    @ValueSource(strings = {RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3, COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1})
    void testResolve(String script) {
        String resolved = RESULT_RESOLVER.resolve(script, Map.of());
        Assertions.assertTrue(resolved.contains(RESULT_PROPERTIES.appearance()));
        Assertions.assertTrue(resolved.contains(RESULT_PROPERTIES.appearance() + " = json.dumps(test_var)"));
        Assertions.assertTrue(resolved.contains("print('" + RESULT_PROPERTIES.appearance() + "' + " + RESULT_PROPERTIES.appearance() + ")"));
    }
}
