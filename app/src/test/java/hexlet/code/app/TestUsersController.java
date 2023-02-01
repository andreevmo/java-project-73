package hexlet.code.app;

import hexlet.code.app.config.jwt.JwtUtils;
import hexlet.code.app.domain.model.User;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@WithMockUser(username = "ivan@google.com")
@Sql(value = {"/import.sql"})
class TestUsersController {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    private static List<User> usersForTest;
    private final String baseUrl = "http://localhost:5000/api";
    private String body = """
                {
                "email": "maxim_525@mail.ru",
                "firstName": "Maxim",
                "lastName": "Andreev",
                "password": "some-password1"
                }
                """;

    @BeforeEach
    void beforeEach() {
        usersForTest = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void test() throws Exception {
        mockMvc.perform(get(baseUrl + "/welcome"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Spring"));
    }

    @Test
    void testGetUserPositive() throws Exception {
        String answer = mockMvc.perform(get(baseUrl + "/users/2"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        assertThat(answer).contains(usersForTest.get(1).getEmail());
        assertThat(answer).contains(usersForTest.get(1).getFirstName());
        assertThat(answer).contains(usersForTest.get(1).getLastName());
        assertThat(answer).contains("createdAt");
        assertThat(answer).doesNotContain("password");
    }

    @Test
    void testGetUserNegative() throws Exception {
        mockMvc.perform(get(baseUrl + "/users/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUsers() throws Exception {
        String response = mockMvc
                .perform(get(baseUrl + "/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        User firstUser = usersForTest.get(0);
        assertThat(response).contains(firstUser.getEmail());
        assertThat(response).doesNotContain("password");
        assertThat(response).contains(firstUser.getFirstName());
        assertThat(response).contains(firstUser.getLastName());
        assertThat(response).contains("createdAt");

        User secondUser = usersForTest.get(1);
        assertThat(response).contains(secondUser.getEmail());
        assertThat(response).doesNotContain("password");
        assertThat(response).contains(secondUser.getFirstName());
        assertThat(response).contains(secondUser.getLastName());
        assertThat(response).contains("createdAt");
    }

    @Test
    void testPostUser() throws Exception {
        String response = mockMvc
                .perform(post(baseUrl + "/users")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(response).contains("maxim_525@mail.ru");
        assertThat(response).doesNotContain("password");
        assertThat(response).contains("Maxim");
        assertThat(response).contains("Andreev");
        assertThat(response).contains("createdAt");
    }

    @Test
    void testPostUserNegative() throws Exception {
        mockMvc.perform(post(baseUrl + "/users")
                        .content(body.replace("some-password1", "so"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPutUser() throws Exception {
        String answer = mockMvc
                .perform(put(baseUrl + "/users/1")
                        .content(body)
                        .header("Authorization", jwtUtils.generateJwtToken("ivan@google.com"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(answer).contains("maxim_525@mail.ru");
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains("Maxim");
        assertThat(answer).contains("Andreev");
        assertThat(answer).contains("createdAt");
    }

    @Test
    void testPutUserNegative() throws Exception {
        mockMvc.perform(put(baseUrl + "/users/1")
                        .content(body.replace("Andreev", ""))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete(baseUrl + "/users/1"))
                .andExpect(status().isOk());
        assertThat(userRepository.existsById(1L)).isFalse();
    }
}
