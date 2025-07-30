package io.w4t3rcs.python.processor;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.resolver.PythonResolver;
import io.w4t3rcs.python.resolver.PythonResolverHolder;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Basic Processor implementation that processes default behavior of declared beans:
 * {@link PythonExecutor} and {@link PythonResolver}
 */
@RequiredArgsConstructor
public class BasicPythonProcessor implements PythonProcessor {
    private final PythonFileHandler pythonFileHandler;
    private final PythonExecutor pythonExecutor;
    private final PythonResolverHolder pythonResolverHolder;

    @Override
    public <R> R process(String script, Class<? extends R> resultClass, Map<String, Object> arguments) {
        String resolvedScript = script;
        if (pythonFileHandler.isPythonFile(script)) resolvedScript = pythonFileHandler.readScriptBodyFromFile(script);
        resolvedScript = pythonResolverHolder.resolveAll(resolvedScript, arguments);
        return pythonExecutor.execute(resolvedScript, resultClass);
    }
}
