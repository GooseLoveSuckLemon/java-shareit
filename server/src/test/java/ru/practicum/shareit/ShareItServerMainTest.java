package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShareItServerMainTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethod_ShouldStartApplication() {
        String[] args = {};
        ShareItServer.main(args);
    }
}