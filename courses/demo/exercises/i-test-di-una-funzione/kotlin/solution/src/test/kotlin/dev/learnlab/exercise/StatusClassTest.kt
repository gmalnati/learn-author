package dev.learnlab.exercise

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

/**
 * I test della classe dei codici di stato. I bug si nascondono ai confini degli intervalli, e i test
 * controllano il primo e l'ultimo codice di ogni classe.
 */
class StatusClassTest {
    @ParameterizedTest
    @CsvSource(
        "100, informativo",
        "199, informativo",
        "200, successo",
        "299, successo",
        "300, redirezione",
        "399, redirezione",
        "400, errore del client",
        "499, errore del client",
        "500, errore del server",
        "599, errore del server",
    )
    fun `ogni codice ha la sua classe, anche ai confini`(code: Int, expected: String) {
        assertThat(statusClass(code)).isEqualTo(expected)
    }

    @Test
    fun `i codici fuori dall'intervallo non sono codici di stato`() {
        assertThatThrownBy { statusClass(99) }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { statusClass(600) }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
