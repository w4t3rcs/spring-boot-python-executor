package io.w4t3rcs.python.local.impl;

import io.w4t3rcs.python.exception.PythonReadingException;
import io.w4t3rcs.python.local.ProcessHandler;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implementation of the {@link ProcessHandler} interface that handles standard output from processes.
 * This class reads the standard output stream of a process, extracts result data marked with
 * a specific prefix, and optionally logs all output lines.
 * 
 * <p>It is typically used to capture the results of Python script execution.</p>
 */
@Slf4j
@RequiredArgsConstructor
public class InputProcessHandler implements ProcessHandler<String> {
    private final PythonExecutorProperties executorProperties;
    private final PythonResolverProperties resolverProperties;

    @Override
    public String handle(Process process) {
        var localProperties = executorProperties.local();
        var resultProperties = resolverProperties.result();
        AtomicReference<String> result = new AtomicReference<>();
        try (BufferedReader bufferedReader = process.inputReader()) {
            bufferedReader.lines().forEach(line -> {
                if (line.contains(resultProperties.appearance())) {
                    String resultJson = line.replace(resultProperties.appearance(), "");
                    result.set(resultJson);
                }
                if (localProperties.loggable()) {
                    log.info(line);
                }
            });
        } catch (IOException e) {
            throw new PythonReadingException(e);
        }

        return result.get();
    }
}
