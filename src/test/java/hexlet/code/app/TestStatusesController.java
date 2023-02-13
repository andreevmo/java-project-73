package hexlet.code.app;


import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.domain.model.Status;
import hexlet.code.app.repository.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static hexlet.code.app.TestUtils.BODY_FOR_TEST_STATUSES;
import static hexlet.code.app.TestUtils.TEST_PATH;
import static hexlet.code.app.TestUtils.performRequest;
import static hexlet.code.app.controller.StatusController.STATUS_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
    @Autowired
    private ObjectMapper mapper;
    private final String baseUrl = TEST_PATH + STATUS_CONTROLLER_PATH;
    private List<Status> statusList;

    @BeforeEach
    void beforeEach() {
        statusList = (List<Status>) statusRepository.findAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetStatus() throws Exception {
        String answer = performRequest(mockMvc, baseUrl + "/1", HttpMethod.GET, status().isOk());
        assertThat(answer).contains(mapper.writeValueAsString(statusList.get(0)));
    }

    @Test
    void testGetStatuses() throws Exception {
        String answer = performRequest(mockMvc, baseUrl, HttpMethod.GET, status().isOk());
        assertThat(answer).isEqualTo(mapper.writeValueAsString(statusList));
    }

    @Test
    void testPostStatus() throws Exception {
        String answer = performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isOk(), BODY_FOR_TEST_STATUSES);
        Status status = statusRepository.findById(5L).orElseThrow();
        assertThat(status.getName()).isEqualTo("Closed");
        assertThat(answer).contains(mapper.writeValueAsString(status));
    }

    @Test
    void testPostStatusNegative() throws Exception {
        String incorrectBody = BODY_FOR_TEST_STATUSES.replace("Closed", "");
        performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testPutStatusNegative() throws Exception {
        String incorrectBody = BODY_FOR_TEST_STATUSES.replace("Closed", "");
        performRequest(mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testPutStatus() throws Exception {
        String oldName = statusList.get(0).getName();
        String answer = performRequest(
                mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isOk(), BODY_FOR_TEST_STATUSES);
        Status status = statusRepository.findById(1L).orElseThrow();
        assertThat(status.getName()).isNotEqualTo(oldName).isEqualTo("Closed");
        assertThat(answer).contains(mapper.writeValueAsString(status));
    }

    @Test
    void testDeleteStatus() throws Exception {
        performRequest(mockMvc, baseUrl + "/1", HttpMethod.DELETE, status().isOk());
        assertThat(statusRepository.existsById(1L)).isFalse();
    }
}