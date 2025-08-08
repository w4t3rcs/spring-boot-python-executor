package io.w4t3rcs.python.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation to specify multiple {@link PythonAfter} annotations on a method.
 * <p>
 * When a method is annotated with {@code @PythonAfters}, each specified Python script
 * will be executed sequentially after the method completes.
 * </p>
 * <p>
 * This allows multiple post-execution Python scripts to be associated with a single method.
 * </p>
 * <p>
 * Example usage:
 * <pre>{@code
 * @PythonAfters({
 *     @PythonAfter("my_script.py"),
 *     @PythonAfter("print(2 + 2)")
 * })
 * public void myMethod() {
 *     // method implementation
 * }
 * }</pre>
 * </p>
 *
 * @see PythonAfter
 * @author w4t3rcs
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PythonAfters {
    /**
     * Array of {@link PythonAfter} annotations specifying the Python scripts
     * to execute after the annotated method.
     * <p>
     * The array must not be {@code null} and must contain at least one element.
     * Each element specifies either inline Python code or a path to a Python file.
     * </p>
     *
     * @return an array of {@link PythonAfter} annotations, never {@code null}
     */
    PythonAfter[] value();
}