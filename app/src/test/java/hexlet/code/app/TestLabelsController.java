package hexlet.code.app;

import hexlet.code.app.domain.model.Label;
import hexlet.code.app.repository.LabelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.StreamSupport;

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
@TestPropertySource("/application-test.properties")
@Sql(value = {"/importSQL/importLabels.sql"})
public class TestLabelsController {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private LabelRepository labelRepository;
    private final String baseUrl = "http://localhost:5000/api" + LABEL_CONTROLLER_PATH;
    private List<Label> labelList;
    private final String body = """
                {
                "name": "bug"
                }
                """;

    @BeforeEach
    void beforeEach() {
        labelList = StreamSupport.stream(labelRepository.findAll().spliterator(), false)
                .toList();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetStatus() throws Exception {
        String answer = mockMvc.perform(get(baseUrl + "/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertThat(answer).contains(labelList.get(0).getName());
    }

    @Test
    void testGetStatuses() throws Exception {
        String answer = mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertThat(answer).contains(
                labelList.get(0).getName(),
                labelList.get(1).getName()
        );
    }

    @Test
    void testPostStatus() throws Exception {
        String answer = mockMvc.perform(
                        post(baseUrl)
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertThat(answer).contains("bug");
    }

    @Test
    void testPostStatusNegative() throws Exception {
        mockMvc.perform(
                        post(baseUrl)
                                .content(body.replace("bug", ""))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPutStatusNegative() throws Exception {
        mockMvc.perform(
                        put(baseUrl + "/1")
                                .content(body.replace("bug", ""))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPutStatus() throws Exception {
        String answer = mockMvc.perform(
                        put(baseUrl + "/1")
                                .content(body)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertThat(answer).contains("bug");
    }

    @Test
    void testDeleteStatus() throws Exception {
        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(status().isOk());
        assertThat(labelRepository.existsById(1L)).isFalse();
    }
}
