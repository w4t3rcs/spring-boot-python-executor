package io.w4t3rcs.python.aspect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.concurrent.atomic.AtomicInteger;

import static io.w4t3rcs.python.constant.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class BasicProfileCheckerTests {
    @InjectMocks
    private BasicProfileChecker basicProfileChecker;
    @Mock
    private Environment environment;

    @Test
    void testDoOnProfiles() {
        Mockito.when(environment.acceptsProfiles(PROFILES_OBJECT)).thenReturn(true);

        AtomicInteger result = new AtomicInteger();
        basicProfileChecker.doOnProfiles(TEST_PROFILES, result::getAndIncrement);
        Assertions.assertEquals(1, result.get());
    }

    @Test
    void testDoOnProfilesEmpty() {
        AtomicInteger result = new AtomicInteger();
        basicProfileChecker.doOnProfiles(EMPTY_PROFILES, result::getAndIncrement);
        Assertions.assertEquals(1, result.get());
    }
}
