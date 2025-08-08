package io.w4t3rcs.python.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to assign a custom name to a method parameter for use in Python scripts.
 * <p>
 * When a method parameter is annotated with {@code @PythonParam}, the specified name
 * can be referenced within Python scripts executed by {@link PythonBefore} or {@link PythonAfter}
 * annotations.
 * </p>
 * <p>
 * If the annotation is absent, the actual parameter name (if available via reflection) will be used.
 * </p>
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * @PythonAfter("my_script.py")
 * public void myMethod(@PythonParam("userId") Long id, String name) {
 *     // method implementation
 * }
 * }</pre>
 * <p>
 * <b>Example Python script snippet using SpEL expressions:</b>
 * <pre>{@code
 * # Access the userId parameter
 * user_id = spel{#userId}
 *
 * # Access the name parameter
 * user_name = spel{#name}
 * }</pre>
 * </p>
 *
 * @author w4t3rcs
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PythonParam {
    /**
     * Specifies the custom name to be used for the annotated parameter in Python scripts.
     * <p>
     * Must not be {@code null} or empty.
     * </p>
     *
     * @return the name used to reference the parameter in Python scripts (non-{@code null}, non-empty)
     */
    String value();
}