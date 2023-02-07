package hexlet.code.app;


import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.domain.model.Task;
import hexlet.code.app.domain.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
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

import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@WithMockUser(username = "ivan@google.com")
@Sql(value = {"/importSQL/importUsers.sql", "/importSQL/importStatuses.sql", "/importSQL/importTasks.sql"})
public class TestTaskController {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper mapper;
    private List<Task> tasks;
    private final String baseUrl = "http://localhost:5000/api" + TASK_CONTROLLER_PATH;
    private final String body = """
                {
                    "name": "Новое имя",
                    "description": "Новое описание",
                    "executorId": 2,
                    "taskStatusId": 2
                }
                """;

    @BeforeEach
    void beforeEach() {
        tasks = StreamSupport.stream(taskRepository.findAll().spliterator(), false).toList();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetTasks() throws Exception {
        String answer = mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String firstTask = mapper.writeValueAsString(tasks.get(0));
        assertThat(answer).contains(firstTask);
        String secondTask = mapper.writeValueAsString(tasks.get(1));
        assertThat(answer).contains(secondTask);
    }

    @Test
    void testGetTask() throws Exception {
        String answer = mockMvc.perform(get(baseUrl + "/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String firstTask = mapper.writeValueAsString(tasks.get(0));
        assertThat(answer).contains(firstTask);
    }

    @Test
    void testPutTask() throws Exception {
        String answer = mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String oldTask = mapper.writeValueAsString(tasks.get(0));
        Task updateTask = taskRepository.findById(1L).orElseThrow();
        String updateTaskJson = mapper.writeValueAsString(updateTask);
        assertThat(answer).doesNotContain(oldTask);
        assertThat(answer).contains(updateTaskJson);
    }

    @Test
    void testPutTaskNegative() throws Exception {
        mockMvc.perform(put(baseUrl + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.replace("2", "null")))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPostTask() throws Exception {
        String answer = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        Task task = taskRepository.findById(3L).orElseThrow();
        assertThat(task.getName()).isEqualTo("Новое имя");
        assertThat(task.getDescription()).isEqualTo("Новое описание");
        User executor = userRepository.findById(2L).orElseThrow();
        assertThat(task.getExecutor().getEmail()).isEqualTo(executor.getEmail());
        assertThat(answer).isEqualTo(
                mapper.writeValueAsString(task)
        );
    }

    @Test
    void testPostTaskNegative() throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.replace(
                                "\"name\": \"Новое имя\",",
                                "\"name\": \"\",")
                        ))
                .andExpect(status().isUnprocessableEntity());
        assertThat(taskRepository.existsByName("")).isFalse();
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(status().isOk());
        assertThat(taskRepository.existsById(1L)).isFalse();
    }

    @Test
    @WithMockUser(username = "petr@google.com")
    void testDeleteTaskNegative() throws Exception {
        mockMvc.perform(delete(baseUrl + "/1"))
                .andExpect(status().isForbidden());
        assertThat(taskRepository.existsById(1L)).isTrue();
    }
}
