package io.w4t3rcs.python.resolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static io.w4t3rcs.python.constant.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class BasicPythonResolverHolderTests {
    @InjectMocks
    private BasicPythonResolverHolder basicPythonResolverHolder;
    @Mock
    private PythonResolver pythonResolver;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(basicPythonResolverHolder, "pythonResolvers", List.of(pythonResolver));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3,
            RESULT_SCRIPT_0, RESULT_SCRIPT_1, RESULT_SCRIPT_2, RESULT_SCRIPT_3,
            SPELYTHON_SCRIPT_0, SPELYTHON_SCRIPT_1,
            COMPOUND_SCRIPT_0, COMPOUND_SCRIPT_1,
    })
    void testResolveAll(String script) {
        Mockito.when(pythonResolver.resolve(script, EMPTY_ARGUMENTS)).thenReturn(OK);

        Assertions.assertEquals(OK, basicPythonResolverHolder.resolveAll(script));
    }

    @Test
    void testGetResolvers() {
        Assertions.assertEquals(List.of(pythonResolver), basicPythonResolverHolder.getResolvers());
    }
}
