package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.domain.model.Label;
import hexlet.code.domain.model.Status;
import hexlet.code.domain.model.Task;
import hexlet.code.domain.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
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

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@WithMockUser(username = "ivan@google.com")
@Sql(value = {
    "/importSQL/importUsers.sql",
    "/importSQL/importStatuses.sql",
    "/importSQL/importLabels.sql",
    "/importSQL/importTasks.sql"
})
public class TestTaskController {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ObjectMapper mapper;
    private List<Task> tasks;
    private final String baseUrl = TestUtils.TEST_PATH + TASK_CONTROLLER_PATH;

    @BeforeEach
    void beforeEach() {
        tasks = (List<Task>) taskRepository.findAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetTasks() throws Exception {
        String answer = TestUtils.performRequest(mockMvc, baseUrl, HttpMethod.GET, status().isOk());
        String firstTask = mapper.writeValueAsString(tasks.get(0));
        assertThat(answer).contains(firstTask);
        String secondTask = mapper.writeValueAsString(tasks.get(1));
        assertThat(answer).contains(secondTask);
    }

    @Test
    void testGetTask() throws Exception {
        String answer = TestUtils.performRequest(mockMvc, baseUrl + "/1", HttpMethod.GET, status().isOk());
        String firstTask = mapper.writeValueAsString(tasks.get(0));
        assertThat(answer).contains(firstTask);
    }

    @Test
    void testPutTask() throws Exception {
        String answer = TestUtils.performRequest(
                mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isOk(), TestUtils.BODY_FOR_TEST_TASK);
        String oldTask = mapper.writeValueAsString(tasks.get(0));
        Task updateTask = taskRepository.findById(1L).orElseThrow();
        String updateTaskJson = mapper.writeValueAsString(updateTask);
        assertThat(answer).doesNotContain(oldTask);
        assertThat(answer).contains(updateTaskJson);
    }

    @Test
    void testPutTaskNegative() throws Exception {
        String incorrectBody = TestUtils.BODY_FOR_TEST_TASK.replace("2", "null");
        TestUtils.performRequest(
                mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testPostTask() throws Exception {
        String answer = TestUtils.performRequest(
                mockMvc, baseUrl, HttpMethod.POST, status().isCreated(), TestUtils.BODY_FOR_TEST_TASK);
        Task task = taskRepository.findById(3L).orElseThrow();
        assertThat(task.getName()).isEqualTo("Новое имя");
        assertThat(task.getDescription()).isEqualTo("Новое описание");
        User executor = userRepository.findById(2L).orElseThrow();
        assertThat(task.getExecutor().getEmail()).isEqualTo(executor.getEmail());
        assertThat(answer).isEqualTo(mapper.writeValueAsString(task));
    }

    @Test
    void testPostTaskNegative() throws Exception {
        String incorrectBody = TestUtils.BODY_FOR_TEST_TASK.replace("\"Новое имя\",", "\"\",");
        TestUtils.performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isUnprocessableEntity(), incorrectBody);
        assertThat(taskRepository.existsByName("")).isFalse();
    }

    @Test
    void testDeleteTask() throws Exception {
        TestUtils.performRequest(mockMvc, baseUrl + "/2", HttpMethod.DELETE, status().isOk());
        assertThat(taskRepository.existsById(2L)).isFalse();
    }

    @Test
    @WithMockUser(username = "petr@google.com")
    void testDeleteTaskNegative() throws Exception {
        TestUtils.performRequest(mockMvc, baseUrl + "/2", HttpMethod.DELETE, status().isForbidden());
        assertThat(taskRepository.existsById(2L)).isTrue();
    }

    @Test
    void testGetTaskWithFilter() throws Exception {
        String answer = TestUtils.performRequest(mockMvc, baseUrl + "?taskStatus=1&executorId=2&labels=1",
                HttpMethod.GET, status().isOk(), TestUtils.BODY_FOR_TEST_TASK);
        List<Label> labels = (List<Label>) labelRepository.findAll();
        List<Status> statuses = (List<Status>) statusRepository.findAll();
        List<User> executors = (List<User>) userRepository.findAll();
        assertThat(answer).contains(
                mapper.writeValueAsString(statuses.get(0)),
                mapper.writeValueAsString(executors.get(1)),
                mapper.writeValueAsString(labels.get(0))
        );
        assertThat(answer).doesNotContain(
                mapper.writeValueAsString(statuses.get(1)),
                mapper.writeValueAsString(executors.get(0)),
                mapper.writeValueAsString(labels.get(1))
        );
    }
}
