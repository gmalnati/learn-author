package dev.learnlab.exercise

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun `greets by name`() {
        assertEquals("Ciao, Ada!", greeting("Ada"))
    }
}
