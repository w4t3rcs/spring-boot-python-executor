package io.w4t3rcs.python.aspect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(MockitoExtension.class)
public class BasicProfileCheckerTests {
    @InjectMocks
    private BasicProfileChecker basicProfileChecker;
    @Mock
    private Environment environment;

    @Test
    void testDoOnProfiles() {
        String[] testProfiles = {"test"};
        Profiles profiles = Profiles.of(testProfiles);
        Mockito.when(environment.acceptsProfiles(profiles)).thenReturn(true);
        AtomicInteger result = new AtomicInteger();
        basicProfileChecker.doOnProfiles(testProfiles, result::getAndIncrement);
        Assertions.assertEquals(1, result.get());
    }

    @Test
    void testDoOnProfilesEmpty() {
        String[] testProfiles = {};
        AtomicInteger result = new AtomicInteger();
        basicProfileChecker.doOnProfiles(testProfiles, result::getAndIncrement);
        Assertions.assertEquals(1, result.get());
    }
}
