package io.w4t3rcs.python.resolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static io.w4t3rcs.python.constant.TestConstants.*;

class RestrictedPythonResolverTests {
    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1
    })
    void testResolve(String script) {
        String resolved = RESTRICTED_PYTHON_RESOLVER.resolve(script, Map.of());
        Assertions.assertTrue(resolved.contains(RESTRICTED_PYTHON_PROPERTIES.importLine()));
        Assertions.assertTrue(resolved.contains(RESTRICTED_PYTHON_PROPERTIES.codeVariableName()));
        Assertions.assertTrue(resolved.contains(RESTRICTED_PYTHON_PROPERTIES.localVariablesName()));
        Assertions.assertTrue(resolved.contains(RESTRICTED_PYTHON_PROPERTIES.safeResultAppearance()));
        Assertions.assertTrue(resolved.contains("_print_ = PrintCollector"));
    }
}
