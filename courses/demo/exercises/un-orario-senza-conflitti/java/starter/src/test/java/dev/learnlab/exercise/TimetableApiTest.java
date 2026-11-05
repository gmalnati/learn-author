package dev.learnlab.exercise;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** L'orario calcolato all'avvio, letto dall'API. */
@SpringBootTest
@AutoConfigureMockMvc
class TimetableApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void lOrarioNonHaConflitti() throws Exception {
        mockMvc.perform(get("/api/timetable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conflicts").value(0))
                .andExpect(jsonPath("$.lessons.length()").value(4));
    }
}
