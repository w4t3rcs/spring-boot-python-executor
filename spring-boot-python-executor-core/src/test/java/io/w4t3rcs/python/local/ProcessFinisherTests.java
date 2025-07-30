package io.w4t3rcs.python.local;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.w4t3rcs.python.constant.TestConstants.*;

class ProcessFinisherTests {
    private static final ProcessFinisher PROCESS_FINISHER = new ProcessFinisherImpl();

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {SIMPLE_SCRIPT_0, SIMPLE_SCRIPT_1, SIMPLE_SCRIPT_2, SIMPLE_SCRIPT_3})
    void testFinish(String script) {
        Process process = new ProcessBuilder("python", "-c", script).start();
        process.waitFor();
        PROCESS_FINISHER.finish(process);
        Assertions.assertFalse(process.isAlive());
    }
}
