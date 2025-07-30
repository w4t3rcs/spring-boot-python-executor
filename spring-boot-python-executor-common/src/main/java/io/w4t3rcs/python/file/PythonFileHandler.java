package io.w4t3rcs.python.file;

import java.nio.file.Path;
import java.util.function.UnaryOperator;

/**
 * Interface defining operations for handling Python script files.
 * This interface provides methods for reading from and writing to Python script files,
 * as well as utilities for working with Python file paths.
 */
public interface PythonFileHandler {
    /**
     * The standard file extension for Python script files.
     */
    String PYTHON_FILE_FORMAT = ".py";

    /**
     * Checks if the given path points to a Python file.
     *
     * @param path The path to check
     * @return true if the path points to a Python file, false otherwise
     */
    boolean isPythonFile(String path);

    /**
     * Writes a Python script to a file at the specified path.
     *
     * @param path The path where the script should be written
     * @param script The Python script content to write
     */
    void writeScriptBodyToFile(String path, String script);

    /**
     * Writes a Python script to a file at the specified path.
     *
     * @param path The Path object where the script should be written
     * @param script The Python script content to write
     */
    void writeScriptBodyToFile(Path path, String script);

    /**
     * Reads a Python script from a file at the specified path.
     *
     * @param path The path from which to read the script
     * @return The content of the Python script file
     */
    String readScriptBodyFromFile(String path);

    /**
     * Reads a Python script from a file at the specified path.
     *
     * @param path The Path object from which to read the script
     * @return The content of the Python script file
     */
    String readScriptBodyFromFile(Path path);

    /**
     * Reads a Python script from a file and applies a transformation function to it.
     *
     * @param path The path from which to read the script
     * @param mapper A function to transform the script content
     * @return The transformed content of the Python script file
     */
    String readScriptBodyFromFile(String path, UnaryOperator<String> mapper);

    /**
     * Reads a Python script from a file and applies a transformation function to it.
     *
     * @param path The Path object from which to read the script
     * @param mapper A function to transform the script content
     * @return The transformed content of the Python script file
     */
    String readScriptBodyFromFile(Path path, UnaryOperator<String> mapper);

    /**
     * Gets the Path object for a script at the specified path.
     *
     * @param path The path to the script
     * @return A Path object representing the script's location
     */
    Path getScriptPath(String path);
}
