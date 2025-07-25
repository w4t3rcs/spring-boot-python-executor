package io.w4t3rcs.python.processor.impl;

import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.processor.PythonProcessor;
import io.w4t3rcs.python.resolver.PythonResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Basic Processor implementation that processes default behavior of declared beans:
 * {@link PythonExecutor} and {@link PythonResolver}
 */
@Service
@RequiredArgsConstructor
public class PythonProcessorImpl implements PythonProcessor {
    private final PythonFileHandler pythonFileHandler;
    private final PythonExecutor pythonExecutor;
    private final List<PythonResolver> pythonResolvers;

    @Override
    public <R> R process(String script, Class<? extends R> resultClass, Map<String, Object> arguments) {
        String result = script;
        if (pythonFileHandler.isPythonFile(script)) {
            result = pythonFileHandler.readScriptBodyFromFile(script);
        }

        for (PythonResolver pythonResolver : pythonResolvers) {
            result = pythonResolver.resolve(result, arguments);
        }

        return pythonExecutor.execute(result, resultClass);
    }
}
