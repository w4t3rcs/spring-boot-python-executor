package io.w4t3rcs.python.local;

import io.w4t3rcs.python.exception.ProcessStartException;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.properties.PythonExecutorProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the {@link ProcessStarter} interface that starts Python processes.
 * This class is responsible for creating and starting Python processes either from
 * script files or from inline Python code.
 */
@Slf4j
@RequiredArgsConstructor
public class ProcessStarterImpl implements ProcessStarter {
    private static final String COMMAND_HEADER = "-c";
    private final PythonExecutorProperties executorProperties;
    private final PythonFileHandler pythonFileHandler;

    @Override
    public Process start(String script) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            String startCommand = executorProperties.local().startCommand();
            if (pythonFileHandler.isPythonFile(script)) {
                processBuilder.command(startCommand, pythonFileHandler.getScriptPath(script).toString());
            } else {
                processBuilder.command(startCommand, COMMAND_HEADER, script.replace("\"", "\"\""));
            }

            log.info("Python script is going to be executed");
            Process process = processBuilder.start();
            process.waitFor();
            return process;
        } catch (Exception e) {
            throw new ProcessStartException(e);
        }
    }
}
