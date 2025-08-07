package io.w4t3rcs.python.aspect;

/**
 * Interface for handling conditional execution based on active Spring profiles.
 * <p>
 * Implementations of this interface define logic for checking whether one or more
 * specified profiles are active in the current {@link org.springframework.core.env.Environment}.
 * <p>
 * This is particularly useful in aspect-oriented programming (AOP) scenarios where
 * certain behavior should only apply when specific profiles are enabled (e.g. "dev", "prod")
 */
public interface ProfileChecker {
    /**
     * Executes the given {@code action} if one of specified {@code profiles} is active in the given {@code environment} or empty/null.
     *
     * @param profiles the array of profile names that must be active to trigger the action (can be empty or null)
     * @param action the logic to execute if the profile condition is met
     */
    void doOnProfiles(String[] profiles, Runnable action);
}
