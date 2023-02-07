package hexlet.code.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Sql(value = {"/import.sql"})
public class TestAuth {

    @Autowired
    private MockMvc mockMvc;
    private final String baseUrl = "http://localhost:5000/api";

    @Test
    void testWithoutAuthUsers() throws Exception {
        mockMvc.perform(put(baseUrl + "/users/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(baseUrl + "/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testWithoutAuthStatuses() throws Exception {
        mockMvc.perform(post(baseUrl + "/statuses"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(baseUrl + "/statuses/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(baseUrl + "/statuses/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testWithoutAuthTasks() throws Exception {
        mockMvc.perform(post(baseUrl + "/task"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(baseUrl + "/task/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(baseUrl + "/task/1"))
                .andExpect(status().isUnauthorized());
    }
}
