package dev.learnlab.exercise;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void greetsByName() {
        assertThat(Main.greeting("Ada")).isEqualTo("Hello, Ada!");
    }
}
