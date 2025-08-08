package io.w4t3rcs.python.aspect;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Interface for extracting values from annotations present on methods.
 * <p>
 * Typically used in aspects or interceptors that execute Python scripts based on annotation metadata.
 * Implementations extract script-related information such as script content or file paths,
 * and active Spring profiles under which the scripts should run.
 * </p>
 * <p>
 * Defines constants for common annotation attribute names:
 * <ul>
 *   <li>{@link #VALUE_METHOD_NAME} — alias for the script value attribute</li>
 *   <li>{@link #SCRIPT_METHOD_NAME} — explicit script attribute name</li>
 *   <li>{@link #ACTIVE_PROFILES_METHOD_NAME} — attribute specifying active Spring profiles</li>
 * </ul>
 * </p>
 * <p>
 * Extraction logic typically involves resolving the annotation instance on
 * the method represented by the {@link JoinPoint}, then returning a map where
 * keys are script contents or file paths, and values are arrays of profile names.
 * </p>
 *
 * @see SinglePythonScriptExtractor
 * @see MultiPythonScriptExtractor
 * @see PythonAnnotationValueCompounder
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface PythonAnnotationValueExtractor {
    /**
     * The attribute name "value" commonly used as alias for the script source.
     */
    String VALUE_METHOD_NAME = "value";
    /**
     * The attribute name "script" explicitly specifying the Python script or file path.
     */
    String SCRIPT_METHOD_NAME = "script";
    /**
     * The attribute name "activeProfiles" specifying Spring profiles for conditional execution.
     */
    String ACTIVE_PROFILES_METHOD_NAME = "activeProfiles";

    /**
     * Extracts a map of Python script sources and their associated active profiles
     * from the annotation of the specified type on the method invoked by the {@code joinPoint}.
     * <p>
     * The map keys represent Python scripts or file paths, the map values are arrays of
     * Spring profile names under which the script should be executed.
     * Implementations must handle any annotation attribute aliases according to annotation definition.
     *
     * @param joinPoint the join point representing the method invocation, must not be {@code null}
     * @param annotationClass the class of the annotation to extract values from, must not be {@code null}
     * @param <A> the annotation type
     * @return a map of script or file path strings and arrays of active Spring profile names, never {@code null}
     */
    <A extends Annotation> Map<String, String[]> getValue(JoinPoint joinPoint, Class<? extends A> annotationClass);
}