package io.w4t3rcs.python.aspect;

import org.springframework.core.env.Environment;

import java.util.List;

/**
 * Strategy interface for handling conditional execution based on active Spring profiles.
 * <p>
 * Implementations of this interface define logic for checking whether one or more
 * specified profiles are active in the current {@link org.springframework.core.env.Environment}.
 * <p>
 * This is particularly useful in aspect-oriented programming (AOP) scenarios where
 * certain behavior should only apply when specific profiles are enabled (e.g. "dev", "prod")
 */
public interface ProfileHandlerAspect {
    /**
     * Executes the given {@code action} if the specified {@code profiles} are active in the given {@code environment} or empty/null.
     *
     * @param profiles the list of profile names that must be active to trigger the action (may be empty or null)
     * @param environment the Spring environment providing the active profile information
     * @param action the logic to execute if the profile condition is met
     */
    void handleProfiles(List<String> profiles, Environment environment, Runnable action);
}
