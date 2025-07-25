package io.w4t3rcs.python.file.impl;

import io.w4t3rcs.python.exception.PythonScriptPathGettingException;
import io.w4t3rcs.python.exception.PythonScriptReadingFromFileException;
import io.w4t3rcs.python.exception.PythonScriptWritingToFileException;
import io.w4t3rcs.python.file.PythonFileHandler;
import io.w4t3rcs.python.properties.PythonFileProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link PythonFileHandler} interface that provides
 * file operations for Python scripts.
 * 
 * <p>This class handles reading from and writing to Python script files,
 * checking if a file is a Python file, and resolving script paths using
 * the configured Python properties.</p>
 */
@RequiredArgsConstructor
public class BasicPythonFileHandler implements PythonFileHandler {
    @Getter(AccessLevel.PROTECTED)
    private final PythonFileProperties fileProperties;

    @Override
    public boolean isPythonFile(String filename) {
        return filename.endsWith(PYTHON_FILE_FORMAT);
    }

    @Override
    public void writeScriptBodyToFile(String path, String script) {
        writeScriptBodyToFile(Path.of(path), script);
    }

    @Override
    public void writeScriptBodyToFile(Path path, String script) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write(script);
        } catch (IOException e) {
            throw new PythonScriptWritingToFileException(e);
        }
    }

    @Override
    public String readScriptBodyFromFile(String path) {
        return readScriptBodyFromFile(getScriptPath(path));
    }

    @Override
    public String readScriptBodyFromFile(Path path) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new PythonScriptReadingFromFileException(e);
        }
    }

    @Override
    public String readScriptBodyFromFile(String path, UnaryOperator<String> mapper) {
        return readScriptBodyFromFile(getScriptPath(path), mapper);
    }

    @Override
    public String readScriptBodyFromFile(Path path, UnaryOperator<String> mapper) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            return bufferedReader.lines()
                    .map(mapper)
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new PythonScriptReadingFromFileException(e);
        }
    }

    @Override
    public Path getScriptPath(String path) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(fileProperties.path() + path);
            return classPathResource.getFile().toPath();
        } catch (IOException e) {
            throw new PythonScriptPathGettingException(e);
        }
    }
}
