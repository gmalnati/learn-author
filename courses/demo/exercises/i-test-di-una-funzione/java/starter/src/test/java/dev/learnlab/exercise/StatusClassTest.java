package dev.learnlab.exercise;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * I test della classe dei codici di stato: scrivi qui quelli che mancano. Le versioni difettose
 * cambiano solo src/main, mai questa cartella.
 */
class StatusClassTest {
    @Test
    void duecentoEUnSuccesso() {
        assertThat(StatusClass.of(200)).isEqualTo("successo");
    }
}
