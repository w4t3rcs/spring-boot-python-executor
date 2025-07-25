package io.w4t3rcs.python.local.impl;

import io.w4t3rcs.python.local.ProcessFinisher;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of the {@link ProcessFinisher} interface that logs process completion.
 * This class checks the exit code of the process and logs appropriate messages
 * based on whether the process completed successfully or with errors.
 */
@Slf4j
public class ProcessFinisherImpl implements ProcessFinisher {
    @Override
    public void finish(Process process) {
        int exitCode = process.exitValue();
        if (exitCode == 0) log.info("Python script is executed with code: {}", exitCode);
        else log.error("Something went wrong! Python script is executed with code: {}", exitCode);
        process.destroy();
    }
}
