package hexlet.code.utils;

import hexlet.code.model.Label;
import hexlet.code.model.Status;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class TestUtils {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private StatusRepository statusRepository;

    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    public List<Status> getStatuses() {
        return (List<Status>) statusRepository.findAll();
    }

    public List<Label> getLabels() {
        return (List<Label>) labelRepository.findAll();
    }

    public List<Task> getTasks() {
        return (List<Task>) taskRepository.findAll();
    }
    public static final String TEST_PATH = "http://localhost:5000/api";
    public static final String BODY_FOR_TEST_TASK = """
                {
                    "name": "Новое имя",
                    "description": "Новое описание",
                    "executorId": 2,
                    "taskStatusId": 2
                }
                """;
    public static final String BODY_FOR_TEST_USERS = """
                {
                "email": "maxim_525@mail.ru",
                "firstName": "Maxim",
                "lastName": "Andreev",
                "password": "some-password1"
                }
                """;
    public static final String BODY_FOR_TEST_STATUSES = """
                {
                "name": "Closed"
                }
                """;
    public static final String BODY_FOR_TEST_LABELS = """
                {
                "name": "bug"
                }
                """;
    public static final String BODY_FOR_TEST_LOGIN = """
               {
                    "email": "ivan@google.com",
                    "password": "some-password"
               }
               """;

    public String performRequest(
            MockMvc mockMvc, String baseUrl, HttpMethod httpMethod, ResultMatcher status) throws Exception {
        return performRequest(mockMvc, baseUrl, httpMethod, status, "");
    }

    public String performRequest(MockMvc mockMvc, String baseUrl, HttpMethod httpMethod, ResultMatcher status,
                                        String body) throws Exception {
        return mockMvc.perform(getMockHttpServletRequestBuilder(baseUrl, httpMethod, body))
                .andExpect(status)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    private MockHttpServletRequestBuilder getMockHttpServletRequestBuilder(
            String url, HttpMethod httpMethod, String body) {
        return switch (httpMethod.name().toLowerCase()) {
            case "get" -> MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON).content(body);
            case "post" -> MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(body);
            case "put" -> MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON).content(body);
            case "delete" -> MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON).content(body);
            default -> throw new IllegalStateException("Unexpected value: " + httpMethod);
        };
    }

    public void tearDown() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        statusRepository.deleteAll();
        userRepository.deleteAll();
    }
}
