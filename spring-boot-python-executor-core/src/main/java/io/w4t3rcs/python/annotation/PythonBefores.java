package io.w4t3rcs.python.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container annotation to specify multiple {@link PythonBefore} annotations on a method.
 * <p>
 * When a method is annotated with {@code @PythonBefores}, each specified Python script
 * will be executed sequentially before the method is called.
 * </p>
 * <p>
 * This allows associating multiple pre-execution Python scripts with a single method.
 * </p>
 * <p>
 * Example usage:
 * <pre>{@code
 * @PythonBefores({
 *     @PythonBefore("my_script.py"),
 *     @PythonBefore("print(2 + 2)")
 * })
 * public void myMethod() {
 *     // method implementation
 * }
 * }</pre>
 * </p>
 *
 * @see PythonBefore
 * @author w4t3rcs
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PythonBefores {
    /**
     * Array of {@link PythonBefore} annotations specifying the Python scripts
     * to execute before the annotated method.
     * <p>
     * The array must not be {@code null} and must contain at least one element.
     * Each element specifies either inline Python code or a path to a Python file.
     * </p>
     *
     * @return an array of {@link PythonBefore} annotations, never {@code null}
     */
    PythonBefore[] value();
}