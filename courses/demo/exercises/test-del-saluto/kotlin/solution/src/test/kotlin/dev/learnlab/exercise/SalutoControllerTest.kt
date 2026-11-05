package dev.learnlab.exercise

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/** Il livello HTTP: il messaggio nel JSON e il 400 per un nome troppo lungo. */
@SpringBootTest
@AutoConfigureMockMvc
class SalutoControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `risponde con il messaggio in JSON`() {
        mockMvc.get("/saluto?nome=Ada").andExpect {
            status { isOk() }
            jsonPath("$.messaggio") { value("Ciao, Ada!") }
        }
    }

    @Test
    fun `rifiuta un nome troppo lungo con 400`() {
        mockMvc.get("/saluto?nome=${"a".repeat(LUNGHEZZA_MASSIMA + 1)}").andExpect {
            status { isBadRequest() }
        }
    }
}
