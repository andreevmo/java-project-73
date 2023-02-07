package hexlet.code.app;


import hexlet.code.app.domain.model.Status;
import hexlet.code.app.repository.StatusRepository;
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

import static hexlet.code.app.controller.StatusController.STATUS_CONTROLLER_PATH;
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
@Sql(value = {"/importSQL/importStatuses.sql"})
public class TestStatusesController {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private StatusRepository statusRepository;
    private final String baseUrl = "http://localhost:5000/api" + STATUS_CONTROLLER_PATH;
    private List<Status> statusList;
    private final String body = """
                {
                "name": "Closed"
                }
                """;

    @BeforeEach
    void beforeEach() {
        statusList = StreamSupport.stream(statusRepository.findAll().spliterator(), false)
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
        assertThat(answer).contains(statusList.get(0).getName());
    }

    @Test
    void testGetStatuses() throws Exception {
        String answer = mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertThat(answer).contains(
                statusList.get(0).getName(),
                statusList.get(1).getName(),
                statusList.get(2).getName(),
                statusList.get(3).getName()
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
        assertThat(answer).contains("Closed");
    }

    @Test
    void testPostStatusNegative() throws Exception {
        mockMvc.perform(
                        post(baseUrl)
                                .content(body.replace("Closed", ""))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPutStatusNegative() throws Exception {
        mockMvc.perform(
                        put(baseUrl + "/1")
                                .content(body.replace("Closed", ""))
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
        assertThat(answer).contains("Closed");
    }

    @Test
    void testDeleteStatus() throws Exception {
        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(status().isOk());
        assertThat(statusRepository.existsById(1L)).isFalse();
    }
}
