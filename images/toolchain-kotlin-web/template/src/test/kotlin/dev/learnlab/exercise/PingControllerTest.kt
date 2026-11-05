package dev.learnlab.exercise

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class PingControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `answers ping`() {
        mockMvc.get("/ping").andExpect {
            status { isOk() }
            jsonPath("$.status") { value("ok") }
        }
    }
}
