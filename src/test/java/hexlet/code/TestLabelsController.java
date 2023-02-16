package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.domain.model.Label;
import hexlet.code.repository.LabelRepository;
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

import static hexlet.code.TestUtils.BODY_FOR_TEST_LABELS;
import static hexlet.code.TestUtils.TEST_PATH;
import static hexlet.code.TestUtils.performRequest;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
    @Autowired
    private ObjectMapper mapper;
    private final String baseUrl = TEST_PATH + LABEL_CONTROLLER_PATH;
    private List<Label> labelList;

    @BeforeEach
    void beforeEach() {
        labelList = (List<Label>) labelRepository.findAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetStatus() throws Exception {
        String answer = performRequest(mockMvc, baseUrl + "/1", HttpMethod.GET, status().isOk());
        assertThat(answer).contains(mapper.writeValueAsString(labelList.get(0)));
    }

    @Test
    void testGetStatuses() throws Exception {
        String answer = performRequest(mockMvc, baseUrl, HttpMethod.GET, status().isOk());
        assertThat(answer).isEqualTo(mapper.writeValueAsString(labelList));
    }

    @Test
    void testPostStatus() throws Exception {
        String answer = performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isOk(), BODY_FOR_TEST_LABELS);
        Label label = labelRepository.findById(3L).orElseThrow();
        assertThat(label.getName()).isEqualTo("bug");
        assertThat(answer).contains(mapper.writeValueAsString(label));
    }

    @Test
    void testPostStatusNegative() throws Exception {
        String incorrectBody = BODY_FOR_TEST_LABELS.replace("bug", "");
        performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testPutStatusNegative() throws Exception {
        String incorrectBody = BODY_FOR_TEST_LABELS.replace("bug", "");
        performRequest(mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testPutStatus() throws Exception {
        String oldName = labelList.get(0).getName();
        String answer = performRequest(
                mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isOk(), BODY_FOR_TEST_LABELS);
        Label label = labelRepository.findById(1L).orElseThrow();
        assertThat(label.getName()).isNotEqualTo(oldName).isEqualTo("bug");
        assertThat(answer).contains(mapper.writeValueAsString(label));
    }

    @Test
    void testDeleteStatus() throws Exception {
        performRequest(mockMvc, baseUrl + "/1", HttpMethod.DELETE, status().isOk());
        assertThat(labelRepository.existsById(1L)).isFalse();
    }
}
