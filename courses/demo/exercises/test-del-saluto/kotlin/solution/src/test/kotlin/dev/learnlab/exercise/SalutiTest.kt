package dev.learnlab.exercise

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullSource
import org.junit.jupiter.params.provider.ValueSource

/** Un test per regola del README: ognuno scopre almeno una versione difettosa. */
class SalutiTest {
    @Test
    fun `saluta per nome`() {
        assertThat(messaggio("Ada")).isEqualTo("Ciao, Ada!")
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = ["", "   "])
    fun `senza nome, o con un nome vuoto, saluta il mondo`(nome: String?) {
        assertThat(messaggio(nome)).isEqualTo("Ciao, mondo!")
    }

    @Test
    fun `toglie gli spazi intorno al nome`() {
        assertThat(messaggio("  Ada ")).isEqualTo("Ciao, Ada!")
    }

    @Test
    fun `saluta un nome lungo fino al limite e rifiuta quelli più lunghi`() {
        assertThat(messaggio("a".repeat(LUNGHEZZA_MASSIMA))).isNotNull()
        assertThat(messaggio("a".repeat(LUNGHEZZA_MASSIMA + 1))).isNull()
    }
}
