package io.w4t3rcs.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DemoAppApplicationTests {

    @Test
    void contextLoads() {
    }

}
