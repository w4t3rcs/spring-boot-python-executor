package io.w4t3rcs.python.file;

import org.junit.jupiter.api.*;

import static io.w4t3rcs.python.constant.TestConstants.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BasicPythonFileHandlerTests {
    @Test
    @Order(0)
    void testCheckPythonFile() {
        boolean isPythonFile = FILE_HANDLER.isPythonFile(FILE_READ_SCRIPT);
        Assertions.assertTrue(isPythonFile);
    }

    @Test
    @Order(1)
    void testReadScriptBodyFromFile() {
        String body = FILE_HANDLER.readScriptBodyFromFile(FILE_READ_SCRIPT);
        Assertions.assertEquals("print(2 + 2)", body);
    }

    @Test
    @Order(2)
    void testReadScriptBodyFromFileWithMapper() {
        String body = FILE_HANDLER.readScriptBodyFromFile(FILE_READ_SCRIPT, String::toUpperCase);
        Assertions.assertEquals("PRINT(2 + 2)", body);
    }

    @Test
    @Order(3)
    void testWriteScriptBodyToFile() {
        FILE_HANDLER.writeScriptBodyToFile(FILE_WRITE_SCRIPT, "print(3 + 3)");
        String body = FILE_HANDLER.readScriptBodyFromFile(FILE_WRITE_SCRIPT);
        Assertions.assertEquals("print(3 + 3)", body);
    }
}
