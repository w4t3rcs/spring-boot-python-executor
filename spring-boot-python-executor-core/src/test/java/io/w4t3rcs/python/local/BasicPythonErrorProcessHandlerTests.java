package io.w4t3rcs.python.local;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.w4t3rcs.python.constant.TestConstants.*;

class BasicPythonErrorProcessHandlerTests {
    private static final ProcessHandler<Void> ERROR_PROCESS_HANDLER = new BasicPythonErrorProcessHandler();

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {BAD_SCRIPT_0, BAD_SCRIPT_1, BAD_SCRIPT_2, BAD_SCRIPT_3})
    void testHandle(String script) {
        Process process = new ProcessBuilder("python", "-c", script).start();
        process.waitFor();
        Void handled = ERROR_PROCESS_HANDLER.handle(process);
        Assertions.assertNull(handled);
        Assertions.assertNotEquals(0, process.exitValue());
    }
}
