package io.w4t3rcs.python.file;

import io.w4t3rcs.python.exception.PythonScriptPathGettingException;
import io.w4t3rcs.python.exception.PythonScriptReadingFromFileException;
import io.w4t3rcs.python.exception.PythonScriptWritingToFileException;
import io.w4t3rcs.python.properties.PythonFileProperties;
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
 * Implementation of the {@link PythonFileHandler} interface providing
 * file operations for Python scripts.
 * <p>
 * This class supports reading from and writing to Python script files,
 * verifying if a filename corresponds to a Python file, and resolving script paths
 * based on configured {@link PythonFileProperties}.
 * </p>
 * <p>
 * The class assumes script files are encoded in the platform default charset.
 * The returned script content preserves line breaks as newline characters.
 * </p>
 *
 * @see PythonFileHandler
 * @see PythonFileProperties
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class BasicPythonFileHandler implements PythonFileHandler {
    private final PythonFileProperties fileProperties;

    /**
     * Checks if the given filename has the Python file extension.
     *
     * @param filename the filename to check, must be non-null
     * @return {@code true} if the filename ends with the Python file extension, {@code false} otherwise
     */
    @Override
    public boolean isPythonFile(String filename) {
        return filename.endsWith(PYTHON_FILE_FORMAT);
    }

    /**
     * Writes the given Python script content to a file resolved from the given path string.
     *
     * @param path the relative path string for the script file, must be non-null
     * @param script the Python script content to write, must be non-null
     * @throws PythonScriptWritingToFileException if an I/O error occurs during writing
     */
    @Override
    public void writeScriptBodyToFile(String path, String script) {
        this.writeScriptBodyToFile(this.getScriptPath(path), script);
    }

    /**
     * Writes the given Python script content to the specified file path.
     *
     * @param path the {@link Path} to the script file, must be non-null and writable
     * @param script the Python script content to write, must be non-null
     * @throws PythonScriptWritingToFileException if an I/O error occurs during writing
     */
    @Override
    public void writeScriptBodyToFile(Path path, String script) {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path)) {
            bufferedWriter.write(script);
        } catch (IOException e) {
            throw new PythonScriptWritingToFileException(e);
        }
    }

    /**
     * Reads the content of a Python script file resolved from the given path string.
     *
     * @param path the relative path string for the script file, must be non-null
     * @return the script content as a {@link String}, never null but possibly empty
     * @throws PythonScriptReadingFromFileException if an I/O error occurs during reading
     */
    @Override
    public String readScriptBodyFromFile(String path) {
        return this.readScriptBodyFromFile(this.getScriptPath(path));
    }

    /**
     * Reads the content of a Python script file from the specified {@link Path}.
     *
     * @param path the {@link Path} to the script file, must be non-null and readable
     * @return the script content as a {@link String}, never null but possibly empty
     * @throws PythonScriptReadingFromFileException if an I/O error occurs during reading
     */
    @Override
    public String readScriptBodyFromFile(Path path) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new PythonScriptReadingFromFileException(e);
        }
    }

    /**
     * Reads the content of a Python script file resolved from the given path string,
     * applying a mapping function to each line.
     *
     * @param path the relative path string for the script file, must be non-null
     * @param mapper a non-null {@link UnaryOperator} to transform each line before joining
     * @return the mapped script content as a {@link String}, never null but possibly empty
     * @throws PythonScriptReadingFromFileException if an I/O error occurs during reading
     */
    @Override
    public String readScriptBodyFromFile(String path, UnaryOperator<String> mapper) {
        return this.readScriptBodyFromFile(this.getScriptPath(path), mapper);
    }

    /**
     * Reads the content of a Python script file from the specified {@link Path},
     * applying a mapping function to each line.
     *
     * @param path the {@link Path} to the script file, must be non-null and readable
     * @param mapper a non-null {@link UnaryOperator} to transform each line before joining
     * @return the mapped script content as a {@link String}, never null but possibly empty
     * @throws PythonScriptReadingFromFileException if an I/O error occurs during reading
     */
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

    /**
     * Resolves the {@link Path} for the script file by appending the given relative path
     * to the base path configured in {@link PythonFileProperties}.
     *
     * @param path the relative path string of the script file, must be non-null
     * @return the resolved absolute {@link Path} to the script file
     * @throws PythonScriptPathGettingException if the resource cannot be resolved as a file path
     */
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