package io.w4t3rcs.python.aspect;

import org.springframework.core.env.Environment;

/**
 * Interface for conditional execution of logic based on active Spring profiles.
 * <p>
 * Implementations check if one or more specified profiles are active in the current
 * Spring {@link Environment} and execute the provided action if the condition is met.
 * </p>
 * <p>
 * This interface is useful in Aspect-Oriented Programming (AOP) or configuration scenarios,
 * where behavior should be applied selectively based on environment profiles such as
 * {@code "dev"}, {@code "prod"}, etc.
 * </p>
 * <p><b>Behavior when profiles are empty or null:</b> the action is executed unconditionally.</p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * profileChecker.doOnProfiles(new String[] {"dev", "test"}, () -> {
 *     // logic to execute only in "dev" or "test" profiles
 * });
 * }</pre>
 *
 * @see Environment
 * @see BasicProfileChecker
 * @author w4t3rcs
 * @since 1.0.0
 */
public interface ProfileChecker {
    /**
     * Executes the given {@code action} if any of the specified {@code profiles} are active.
     * <p>
     * If {@code profiles} is {@code null} or empty, the {@code action} is always executed.
     * Otherwise, the method checks whether at least one profile in {@code profiles}
     * is active in the current Spring {@link Environment}.
     * </p>
     *
     * @param profiles an array of profile names to check; may be {@code null} or empty
     * @param action the {@link Runnable} to execute if the profile condition is satisfied; must not be {@code null}
     */
    void doOnProfiles(String[] profiles, Runnable action);
}
