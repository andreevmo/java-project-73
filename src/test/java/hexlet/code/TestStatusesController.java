package hexlet.code;


import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Status;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
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

import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BODY_FOR_TEST_STATUSES;
import static hexlet.code.utils.TestUtils.TEST_PATH;
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
    private ObjectMapper mapper;

    @Autowired
    private TestUtils testUtils;
    private final String baseUrl = TEST_PATH + STATUS_CONTROLLER_PATH;
    private List<Status> statusList;

    @BeforeEach
    void beforeEach() {
        statusList = testUtils.getStatuses();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void clear() {
        testUtils.tearDown();
    }

    @Test
    void testGetStatus() throws Exception {
        String answer = testUtils.performRequest(mockMvc, baseUrl + "/1", HttpMethod.GET, status().isOk());
        assertThat(answer).contains(mapper.writeValueAsString(statusList.get(0)));
    }

    @Test
    void testGetStatuses() throws Exception {
        String answer = testUtils.performRequest(mockMvc, baseUrl, HttpMethod.GET, status().isOk());
        assertThat(answer).isEqualTo(mapper.writeValueAsString(statusList));
    }

    @Test
    void testPostStatus() throws Exception {
        String answer = testUtils.performRequest(
                mockMvc, baseUrl, HttpMethod.POST, status().isCreated(), BODY_FOR_TEST_STATUSES);
        Status status = testUtils.getStatuses().get(4);
        assertThat(status.getName()).isEqualTo("Closed");
        assertThat(answer).contains(mapper.writeValueAsString(status));
    }

    @Test
    void testPostStatusNegative() throws Exception {
        String incorrectBody = BODY_FOR_TEST_STATUSES.replace("Closed", "");
        testUtils.performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testPutStatusNegative() throws Exception {
        String incorrectBody = BODY_FOR_TEST_STATUSES.replace("Closed", "");
        testUtils.performRequest(
                mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testPutStatus() throws Exception {
        String oldName = statusList.get(0).getName();
        String answer = testUtils.performRequest(
                mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isOk(), BODY_FOR_TEST_STATUSES);
        Status status = testUtils.getStatuses().get(0);
        assertThat(status.getName()).isNotEqualTo(oldName).isEqualTo("Closed");
        assertThat(answer).contains(mapper.writeValueAsString(status));
    }

    @Test
    void testDeleteStatus() throws Exception {
        Status status = statusList.get(0);
        testUtils.performRequest(mockMvc, baseUrl + "/1", HttpMethod.DELETE, status().isOk());
        assertThat(testUtils.getStatuses().contains(status)).isFalse();
    }
}
