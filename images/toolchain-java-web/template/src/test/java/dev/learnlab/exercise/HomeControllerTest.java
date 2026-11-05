package dev.learnlab.exercise;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void showsThePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<h1>Ciao, mondo!</h1>")))
                .andExpect(content().string(containsString("htmx.min.js")));
    }

    @Test
    void escapesTheNameInTheFragment() throws Exception {
        mockMvc.perform(get("/greeting").param("name", "<b>Ada</b>"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("&lt;b")))
                .andExpect(content().string(not(containsString("<b>"))));
    }

    @Test
    void servesHtmx() throws Exception {
        mockMvc.perform(get("/webjars/htmx.org/2.0.11/dist/htmx.min.js")).andExpect(status().isOk());
    }
}
