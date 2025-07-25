package io.w4t3rcs.python.local.impl;

import io.w4t3rcs.python.exception.PythonReadingException;
import io.w4t3rcs.python.local.ProcessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ProcessHandler} interface that handles error output from processes.
 * This class reads the error stream of a process, logs any error messages, and returns null.
 * It is typically used to capture and log error messages from Python script execution.
 */
@Slf4j
@Service
public class ErrorProcessHandler implements ProcessHandler<Void> {
    @Override
    public Void handle(Process process) {
        try (BufferedReader bufferedReader = process.errorReader()) {
            String errorMessage = bufferedReader.lines().collect(Collectors.joining());
            if (!errorMessage.isBlank()) {
                log.error(errorMessage);
            }
        } catch (IOException e) {
            throw new PythonReadingException(e);
        }

        return null;
    }
}
