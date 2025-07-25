package io.w4t3rcs.python.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable Py4J integration in a Spring Boot application.
 * 
 * <p>This annotation can be applied to a configuration class to enable
 * the Py4J bridge between Java and Python. When this annotation is present,
 * the Py4J-related beans will be created and the bridge will be established.</p>
 * 
 * <p>Example usage:</p>
 * <pre>
 * &#64;EnablePy4J
 * &#64;Configuration
 * public class MyConfig {
 *     // Configuration code
 * }
 * </pre>
 * 
 * <p>Alternatively, Py4J integration can be enabled by setting the
 * "spring.python.py4j.enabled" property to "true" in the application properties.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnablePy4J {
}
