package io.w4t3rcs.python.constant;

import io.w4t3rcs.python.dto.PythonExecutionResponse;

import java.nio.file.Path;
import java.util.Map;

public class TestConstants {
    public static final String EMPTY = "";
    public static final String OK = "OK";
    public static final PythonExecutionResponse<String> OK_RESPONSE = new PythonExecutionResponse<>(OK);
    public static final Class<? extends String> STRING_CLASS = String.class;
    public static final Class<? extends PythonExecutionResponse<String>> STRING_RESPONSE_CLASS = (Class<? extends PythonExecutionResponse<String>>) OK_RESPONSE.getClass();
    public static final Class<? extends Path> PATH_CLASS = Path.class;
    public static final Map<String, Object> EMPTY_ARGUMENTS = Map.of();
    public static final String SIMPLE_SCRIPT_0 = "print(2 + 2)";
    public static final String SIMPLE_SCRIPT_1 = "test_var1 = 2 + 2\ntest_var2 = 6 + 2\nprint(test_var1 + test_var2)";
    public static final String SIMPLE_SCRIPT_2 = "test_var = 'hello world'\nprint(test_var)";
    public static final String SIMPLE_SCRIPT_3 = "import json\ntest_var = 2 + 2\nprint('r4java' + json.dumps(test_var))";
    public static final String RESULT_SCRIPT_0 = "test_var = 2 + 2\no4java{test_var}";
    public static final String RESULT_SCRIPT_1 = "import json\ntest_var = 2 + 2\no4java{test_var}";
    public static final String RESULT_SCRIPT_2 = "test_var = 'hello world'\nprint(test_var)\no4java{test_var}";
    public static final String RESULT_SCRIPT_3 = "test_var = {'x': 2, 'y': 7}\nprint(test_var)\no4java{test_var}";
    public static final String SPELYTHON_SCRIPT_0 = "print(spel{#a})";
    public static final String SPELYTHON_SCRIPT_1 = "test_var1 = spel{#a}\ntest_var2 = spel{#b}\nprint(test_var1 + test_var2)";
    public static final String COMPOUND_SCRIPT_0 = "test_var = 'hello world'\nprint(test_var + spel{#a})\no4java{test_var}";
    public static final String COMPOUND_SCRIPT_1 = "import json\ntest_var = {'x': 2, 'y': spel{#b}}\nprint(test_var)\no4java{test_var}";
    public static final String FILE_SCRIPT = "test.py";
    public static final Path FILE_PATH = Path.of(FILE_SCRIPT);
    public static final String CACHE_MANAGER_KEY = "manager_key";
    public static final String CACHE_KEY = "key";
}
