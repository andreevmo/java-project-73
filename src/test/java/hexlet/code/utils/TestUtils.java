package hexlet.code.utils;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

public class TestUtils {

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

    public static String performRequest(
            MockMvc mockMvc, String baseUrl, HttpMethod httpMethod, ResultMatcher status) throws Exception {
        return performRequest(mockMvc, baseUrl, httpMethod, status, "");
    }

    public static String performRequest(MockMvc mockMvc, String baseUrl, HttpMethod httpMethod, ResultMatcher status,
                                        String body) throws Exception {
        return mockMvc.perform(getMockHttpServletRequestBuilder(baseUrl, httpMethod, body))
                .andExpect(status)
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    private static MockHttpServletRequestBuilder getMockHttpServletRequestBuilder(
            String url, HttpMethod httpMethod, String body) {
        return switch (httpMethod.name().toLowerCase()) {
            case "get" -> MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON).content(body);
            case "post" -> MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON).content(body);
            case "put" -> MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON).content(body);
            case "delete" -> MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON).content(body);
            default -> throw new IllegalStateException("Unexpected value: " + httpMethod);
        };
    }
}
