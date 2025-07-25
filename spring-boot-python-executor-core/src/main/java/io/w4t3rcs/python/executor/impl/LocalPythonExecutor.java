package io.w4t3rcs.python.executor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.exception.PythonScriptExecutionException;
import io.w4t3rcs.python.executor.PythonExecutor;
import io.w4t3rcs.python.local.ProcessFinisher;
import io.w4t3rcs.python.local.ProcessHandler;
import io.w4t3rcs.python.local.ProcessStarter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the {@link PythonExecutor} interface that executes Python scripts locally.
 * This class coordinates the process of starting a Python process, handling its input
 * and error streams, and converting the result to the specified Java type.
 */
@Slf4j
@RequiredArgsConstructor
public class LocalPythonExecutor implements PythonExecutor {
    private final ProcessStarter processStarter;
    private final ProcessHandler<String> inputProcessHandler;
    private final ProcessHandler<Void> errorProcessHandler;
    private final ObjectMapper objectMapper;
    private final ProcessFinisher processFinisher;

    @Override
    public <R> R execute(String script, Class<? extends R> resultClass) {
        try {
            Process process = processStarter.start(script);
            process.waitFor();
            String jsonResult = inputProcessHandler.handle(process);
            errorProcessHandler.handle(process);
            processFinisher.finish(process);
            return resultClass == null || jsonResult == null || jsonResult.isBlank()
                    ? null
                    : objectMapper.readValue(jsonResult, resultClass);
        } catch (Exception e) {
            throw new PythonScriptExecutionException(e);
        }
    }
}
