package dev.learnlab.exercise

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * I test della classe dei codici di stato: scrivi qui quelli che mancano. Le versioni difettose
 * cambiano solo src/main, mai questa cartella.
 */
class StatusClassTest {
    @Test
    fun `200 è un successo`() {
        assertThat(statusClass(200)).isEqualTo("successo")
    }
}
