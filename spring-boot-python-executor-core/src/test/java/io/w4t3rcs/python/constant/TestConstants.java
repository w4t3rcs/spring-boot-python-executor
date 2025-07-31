package io.w4t3rcs.python.constant;

import io.w4t3rcs.python.file.BasicPythonFileHandler;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.properties.PythonFileProperties;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import io.w4t3rcs.python.resolver.Py4JResolver;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.resolver.RestrictedPythonResolver;
import io.w4t3rcs.python.resolver.ResultResolver;

import java.util.Map;

import static io.w4t3rcs.python.properties.PythonResolverProperties.*;
import static io.w4t3rcs.python.properties.PythonResolverProperties.DeclaredResolver.*;
import static io.w4t3rcs.python.properties.PythonResolverProperties.SpelythonProperties.SpelProperties;

public final class TestConstants {
    //Script constants
    public static final String OK = "OK";
    public static final Class<? extends String> STRING_CLASS = String.class;
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
    public static final String BAD_SCRIPT_0 = "print(2 + 2) print(2 - 2)";
    public static final String BAD_SCRIPT_1 = "print(2 + 2). print(2 - 2)";
    public static final String BAD_SCRIPT_2 = "import jsonnn\ntest_var = {'x': 2, 'y': 2}\nprint(test_var)";
    public static final String BAD_SCRIPT_3 = "import json\ntest_var = {'x': 2, 'y': 2}\nprint(test_varr)";
    public static final String FILE_SCRIPT = "test.py";

    //Resolver constants
    public static final SpelythonProperties SPELYTHON_PROPERTIES = new SpelythonProperties("spel\\{.+?}", new SpelProperties("#", 5, 1));
    public static final Py4JProperties PY4J_PROPERTIES = new Py4JProperties("from py4j.java_gateway import JavaGateway, GatewayParameters", "gateway = JavaGateway(\n\tgateway_parameters=GatewayParameters(\n\t\t%s\n\t)\n)", new String[]{"address=\"localhost\""});
    public static final RestrictedPythonProperties RESTRICTED_PYTHON_PROPERTIES = new RestrictedPythonProperties("from RestrictedPython import compile_restricted\nfrom RestrictedPython import safe_globals", "source_code", "execution_result", "r4java_restricted", true);
    public static final ResultProperties RESULT_PROPERTIES = new ResultProperties("o4java\\{.+?}", "r4java", 7, 1);
    public static final PythonResolverProperties RESOLVER_PROPERTIES = new PythonResolverProperties(new DeclaredResolver[]{SPELYTHON, PY4J, RESTRICTED_PYTHON, RESULT}, "(^import [\\w.]+$)|(^import [\\w.]+ as [\\w.]+$)|(^from [\\w.]+ import [\\w., ]+$)", SPELYTHON_PROPERTIES, PY4J_PROPERTIES, RESTRICTED_PYTHON_PROPERTIES, RESULT_PROPERTIES);
    public static final PythonResolver PY4J_RESOLVER = new Py4JResolver(RESOLVER_PROPERTIES);
    public static final PythonResolver RESTRICTED_PYTHON_RESOLVER = new RestrictedPythonResolver(RESOLVER_PROPERTIES);
    public static final PythonResolver RESULT_RESOLVER = new ResultResolver(RESOLVER_PROPERTIES);

    //File constants
    public static final PythonFileProperties FILE_PROPERTIES = new PythonFileProperties("/");
    public static final PythonFileHandler FILE_HANDLER = new BasicPythonFileHandler(FILE_PROPERTIES);
}
