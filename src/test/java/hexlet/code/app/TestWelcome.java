package hexlet.code.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import static hexlet.code.app.TestUtils.performRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class TestWelcome {

    @Autowired
    private MockMvc mockMvc;
    @Test
    void testWelcome() throws Exception {
        String answer = performRequest(mockMvc, "http://localhost:5000/welcome", HttpMethod.GET, status().isOk());
        assertThat(answer).contains("Welcome to Spring");
    }
}
