package dev.learnlab.exercise

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * I test del servizio di saluto: scrivi qui quelli che mancano, uno per regola del README. Le
 * versioni difettose cambiano solo src/main, mai questa cartella.
 */
class SalutiTest {
    @Test
    fun `saluta per nome`() {
        assertThat(messaggio("Ada")).isEqualTo("Ciao, Ada!")
    }
}
