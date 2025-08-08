package io.w4t3rcs.python.file;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

/**
 * Defines operations for working with Python script files, including validation, I/O,
 * and content transformation.
 *
 * <p>This interface provides methods for:</p>
 * <ul>
 *     <li>Verifying Python file extensions</li>
 *     <li>Reading and writing Python script files</li>
 *     <li>Applying transformations to script content</li>
 *     <li>Resolving file paths</li>
 * </ul>
 *
 * <p>File format is assumed to use the {@link #PYTHON_FILE_FORMAT} extension ({@code ".py"}).
 * Implementations must handle file system I/O in a defined, consistent manner and
 * should ensure proper resource management (e.g., closing streams, handling encoding).
 * Unless explicitly documented, all methods are expected to be thread-safe.</p>
 *
 * <p><strong>Example usage:</strong></p>
 * <pre>{@code
 * PythonFileHandler handler = ...;
 * if (handler.isPythonFile("script.py")) {
 *     handler.writeScriptBodyToFile("script.py", "print('Hello')");
 *     String content = handler.readScriptBodyFromFile("script.py");
 * }
 * }</pre>
 *
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface PythonFileHandler {
    /**
     * The standard file extension for Python script files.
     */
    String PYTHON_FILE_FORMAT = ".py";

    /**
     * Checks whether the given path points to a valid Python file.
     *
     * @param path non-{@code null} path string
     * @return {@code true} if the path ends with {@link #PYTHON_FILE_FORMAT}, {@code false} otherwise
     */
    boolean isPythonFile(String path);

    /**
     * Writes Python script content to a file at the specified path.
     *
     * @param path non-{@code null} file system path
     * @param script non-{@code null} Python script content
     */
    void writeScriptBodyToFile(String path, String script);

    /**
     * Writes Python script content to a file at the specified path.
     *
     * @param path non-{@code null} {@link Path} object
     * @param script non-{@code null} Python script content
     */
    void writeScriptBodyToFile(Path path, String script);

    /**
     * Reads the full content of a Python script from a file.
     *
     * @param path non-{@code null} file system path
     * @return non-{@code null} script content
     */
    String readScriptBodyFromFile(String path);

    /**
     * Reads the full content of a Python script from a file.
     *
     * @param path non-{@code null} {@link Path} object
     * @return non-{@code null} script content
     */
    String readScriptBodyFromFile(Path path);

    /**
     * Reads the full content of a Python script from a file and applies a transformation function.
     *
     * @param path non-{@code null} file system path
     * @param mapper non-{@code null} transformation function
     * @return non-{@code null} transformed script content
     */
    String readScriptBodyFromFile(String path, UnaryOperator<String> mapper);

    /**
     * Reads the full content of a Python script from a file and applies a transformation function.
     *
     * @param path non-{@code null} {@link Path} object
     * @param mapper non-{@code null} transformation function
     * @return non-{@code null} transformed script content
     */
    String readScriptBodyFromFile(Path path, UnaryOperator<String> mapper);

    /**
     * Returns a {@link Path} object representing the location of the given script.
     *
     * @param path non-{@code null} file system path
     * @return non-{@code null} {@link Path} instance pointing to the script
     */
    Path getScriptPath(String path);
}