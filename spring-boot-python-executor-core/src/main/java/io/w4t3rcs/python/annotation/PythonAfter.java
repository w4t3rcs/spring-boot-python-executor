package io.w4t3rcs.python.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.env.Environment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to execute a Python script after the annotated method completes.
 * <p>
 * When a method is annotated with {@code @PythonAfter}, the specified Python script
 * or file will be executed immediately after the method finishes execution.
 * </p>
 * <p>
 * This annotation supports specifying a Python script as inline code or as a file path.
 * Additionally, execution can be restricted to certain Spring profiles using
 * {@link #activeProfiles()}.
 * </p>
 * <p>
 * Example usage:
 * <pre>{@code
 * @PythonAfter("my_script.py")
 * public void myMethod() {
 *     // method logic
 * }
 * }</pre>
 * </p>
 *
 * @see Environment
 * @author w4t3rcs
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PythonAfter {
    /**
     * Alias for {@link #script()}.
     * <p>
     * Must not be {@code null}/empty or {@link #script()} not be {@code null}/empty.
     * </p>
     *
     * @return the Python script content or file path to execute after the method
     */
    @AliasFor("script")
    String value() default "";

    /**
     * The Python script content or file path to execute after the annotated method completes.
     * <p>
     * If both {@link #value()} and {@code script} are specified, their values must be identical.
     * The value can be either inline Python code or a path to a Python file.
     * </p>
     *
     * @return the Python script or file path (never {@code null} or empty)
     */
    @AliasFor("value")
    String script() default "";

    /**
     * Spring profiles under which this Python script should be executed.
     * <p>
     * If empty, the script executes regardless of active profiles.
     * If one or more profiles are specified, the script will execute only if
     * at least one of these profiles is active in the current
     * {@link Environment}.
     * </p>
     * <p>
     * Implementations should verify active profiles at runtime before executing the script.
     * </p>
     *
     * @return array of profile names, never {@code null}, may be empty
     */
    String[] activeProfiles() default {};
}