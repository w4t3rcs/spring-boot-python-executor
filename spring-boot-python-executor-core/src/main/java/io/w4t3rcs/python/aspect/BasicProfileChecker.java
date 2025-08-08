package io.w4t3rcs.python.aspect;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

/**
 * Basic implementation of {@link ProfileChecker} using Spring {@link Environment}.
 * <p>
 * This implementation executes the provided {@link Runnable} action if the specified
 * profiles array is empty or {@code null}, or if at least one profile is active
 * in the configured {@link Environment}.
 * </p>
 * <p>
 * Profile matching is delegated to Spring's {@link Environment#acceptsProfiles(Profiles)} method
 * using {@link Profiles#of(String...)}.
 * </p>
 * <p><b>Example usage:</b></p>
 * <pre>{@code
 * ProfileChecker checker = new BasicProfileChecker(environment);
 * checker.doOnProfiles(new String[]{"dev", "test"}, () -> {
 *     // code executed only if 'dev' or 'test' profiles are active
 * });
 * }</pre>
 *
 * @see Environment
 * @see ProfileChecker
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class BasicProfileChecker implements ProfileChecker {
    private final Environment environment;

    /**
     * Executes the given {@code action} if {@code profiles} is empty or {@code null},
     * or if any of the specified profiles are active in the {@link Environment}.
     *
     * @param profiles an array of profile names, may be {@code null} or empty
     * @param action the {@link Runnable} to execute if the condition is met, must not be {@code null}
     */
    @Override
    public void doOnProfiles(String[] profiles, Runnable action) {
        if (profiles == null || profiles.length == 0) {
            action.run();
        } else {
            Profiles profilesObject = Profiles.of(profiles);
            if (environment.acceptsProfiles(profilesObject)) {
                action.run();
            }
        }
    }
}
