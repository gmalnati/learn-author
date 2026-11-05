package dev.learnlab.exercise

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class SalutoControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `saluta per nome`() {
        mockMvc.get("/saluto?nome=Ada").andExpect {
            status { isOk() }
            jsonPath("$.messaggio") { value("Ciao, Ada!") }
        }
    }

    @Test
    fun `senza nome saluta il mondo`() {
        mockMvc.get("/saluto").andExpect {
            status { isOk() }
            jsonPath("$.messaggio") { value("Ciao, mondo!") }
        }
    }
}
