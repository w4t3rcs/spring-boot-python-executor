package io.w4t3rcs.python.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to execute a Python script after a method.
 * 
 * <p>When a method is annotated with {@code PythonAfter}, the specified Python script
 * will be executed after the method completes.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * &#64;PythonAfters({
 *     &#64;PythonAfter("my_script.py"),
 *     &#64;PythonAfter("print(2 + 2)"),
 * })
 * public void myMethod() {
 *     // Method implementation
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PythonAfters {
    /**
     * The Python scripts to execute after the method.
     * This can be either the path to a Python file or the actual Python code.
     * 
     * @return the Python scripts or file paths wrapped in {@link PythonAfter} annotation.
     */
    PythonAfter[] value();
}
