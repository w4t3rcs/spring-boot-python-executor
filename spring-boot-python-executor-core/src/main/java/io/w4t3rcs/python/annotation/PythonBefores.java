package io.w4t3rcs.python.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to execute a Python script before a method.
 * 
 * <p>When a method is annotated with {@code PythonBefore}, the specified Python script
 * will be executed before the method is called.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * &#64;PythonBefores({
 *     &#64;PythonBefore("my_script.py"),
 *     &#64;PythonBefore("print(2 + 2)"),
 * })
 * public void myMethod() {
 *     // Method implementation
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PythonBefores {
    /**
     * The Python scripts to execute before the method.
     * This can be either the path to a Python file or the actual Python code.
     * 
     * @return the Python scripts or file paths wrapped in {@link PythonBefore} annotation.
     */
    PythonBefore[] value();
}
