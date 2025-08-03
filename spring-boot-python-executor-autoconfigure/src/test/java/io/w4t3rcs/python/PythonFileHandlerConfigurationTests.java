package io.w4t3rcs.python;

import io.w4t3rcs.python.config.PythonAutoConfiguration;
import io.w4t3rcs.python.file.BasicPythonFileHandler;
import io.w4t3rcs.python.file.PythonFileHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@AutoConfigureJson
@ContextConfiguration(classes = {PythonAutoConfiguration.class})
class PythonFileHandlerConfigurationTests {
    @Autowired
    private PythonFileHandler pythonFileHandler;

    @Test
    void testMandatoryBeansLoad() {
        Assertions.assertInstanceOf(BasicPythonFileHandler.class, pythonFileHandler);
    }
}
