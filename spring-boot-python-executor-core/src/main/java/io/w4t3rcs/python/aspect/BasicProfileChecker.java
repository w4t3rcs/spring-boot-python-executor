package io.w4t3rcs.python.aspect;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@RequiredArgsConstructor
public class BasicProfileChecker implements ProfileChecker {
    private final Environment environment;

    @Override
    public void doOnProfiles(String[] profiles, Runnable action) {
        if (profiles == null || profiles.length == 0) action.run();
        else {
            Profiles profilesObject = Profiles.of(profiles);
            boolean containsProfile = environment.acceptsProfiles(profilesObject);
            if (containsProfile) action.run();
        }
    }
}
