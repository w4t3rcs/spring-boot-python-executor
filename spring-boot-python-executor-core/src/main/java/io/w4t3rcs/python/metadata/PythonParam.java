package io.w4t3rcs.python.metadata;

import io.w4t3rcs.python.resolver.impl.SpelythonResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify a name for a method parameter to be used in Python scripts.
 * 
 * <p>When a parameter is annotated with {@code PythonParam}, the specified name can be used
 * to reference the parameter's value, for example, in SpEL expressions within Python scripts executed by
 * {@link PythonBefore} or {@link PythonAfter} annotations using {@link SpelythonResolver}.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * &#64;PythonAfter("my_script.py")
 * public void myMethod(@SpelythonParam("userId") Long id, @SpelythonParam("userName") String name) {
 *     // Method implementation
 * }
 * </pre>
 * 
 * <p>In the Python script, the parameters can be accessed using SpEL expressions:</p>
 * <pre>
 * # Access the userId parameter
 * user_id = spel{#userId}
 * 
 * # Access the userName parameter
 * user_name = spel{#userName}
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PythonParam {
    /**
     * The name to use for the parameter.
     * If not specified, the parameter's actual name will be used.
     * 
     * @return the name for the parameter
     */
    String value();
}
