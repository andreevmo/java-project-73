package hexlet.code.app;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class AppApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private static List<User> users;

    private User userForTest;
    private static Path path = Path.of("src/test/resources/users");
    private static String baseUrl = "http://localhost:5000";

    @BeforeAll
    static void beforeAll() throws IOException {
        String usersFile = Files.readString(path);
        users = new  ObjectMapper().readValue(usersFile, new TypeReference<>() { });
    }

    @BeforeEach
    void beforeEach() {
        userRepository.saveAll(users);
    }

    @Test
    void test() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/welcome"))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).contains("Welcome to Spring");
    }

    @Test
    void testGetUserPositive() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/users/2"))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        String answer = response.getContentAsString();
        userForTest = users.get(1);
        assertThat(answer).contains(userForTest.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(userForTest.getFirstName());
        assertThat(answer).contains(userForTest.getLastName());
        assertThat(answer).contains("createdAt");
    }

    @Test
    void testGetUserNegative() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/users/10"))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(404);
    }

    @Test
    void testGetUsers() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get(baseUrl + "/users"))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        String answer = response.getContentAsString();
        userForTest = users.get(0);
        assertThat(answer).contains(userForTest.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(userForTest.getFirstName());
        assertThat(answer).contains(userForTest.getLastName());
        assertThat(answer).contains("createdAt");

        userForTest = users.get(1);
        assertThat(answer).contains(userForTest.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(userForTest.getFirstName());
        assertThat(answer).contains(userForTest.getLastName());
        assertThat(answer).contains("createdAt");
    }

    @Test
    void testPostUser() throws Exception {
        userForTest = new User();
        userForTest.setEmail("ivan@google.com");
        userForTest.setFirstName("Ivan");
        userForTest.setLastName("Petrov");
        userForTest.setPassword("some-password");
        String userJson = new ObjectMapper().writeValueAsString(userForTest);
        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        String answer = response.getContentAsString();
        assertThat(answer).contains(userForTest.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(userForTest.getFirstName());
        assertThat(answer).contains(userForTest.getLastName());
        assertThat(answer).contains("createdAt");

        boolean existsNewUser = userRepository.existsByEmail("ivan@google.com");
        assertThat(existsNewUser).isTrue();
    }

    @Test
    void testPostUserNegative() throws Exception {
        userForTest = new User();
        userForTest.setEmail("ivan@google.com");
        userForTest.setFirstName("Ivan");
        userForTest.setLastName("Petrov");
        userForTest.setPassword("so");
        String userJson = new ObjectMapper().writeValueAsString(userForTest);
        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testPutUser() throws Exception {
        userForTest = new User();
        userForTest.setEmail("maxim_525@mail.ru");
        userForTest.setFirstName("Max");
        userForTest.setLastName("Ivanov");
        userForTest.setPassword("12348765");
        String userJson = new ObjectMapper().writeValueAsString(userForTest);
        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/users/1")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        String answer = response.getContentAsString();
        assertThat(answer).contains(userForTest.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(userForTest.getFirstName());
        assertThat(answer).contains(userForTest.getLastName());
        assertThat(answer).contains("createdAt");


        User userFromDB = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User with this id was not found"));
        assertThat(userForTest.getFirstName()).isEqualTo(userFromDB.getFirstName());
        assertThat(userForTest.getLastName()).isEqualTo(userFromDB.getLastName());
        assertThat(userForTest.getEmail()).isEqualTo(userFromDB.getEmail());
    }

    @Test
    void testPutUserNegative() throws Exception {
        userForTest = new User();
        userForTest.setEmail("maxim_525@mail.ru");
        userForTest.setFirstName("Max");
        userForTest.setLastName("");
        userForTest.setPassword("12348765");
        String userJson = new ObjectMapper().writeValueAsString(userForTest);
        MockHttpServletResponse response = mockMvc
                .perform(put(baseUrl + "/users/1")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    void testDeleteUser() throws Exception {
        boolean existsNewUser = userRepository.existsById(1L);
        assertThat(existsNewUser).isTrue();
        MockHttpServletResponse response = mockMvc
                .perform(delete(baseUrl + "/users/1"))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);

        existsNewUser = userRepository.existsById(1L);
        assertThat(existsNewUser).isFalse();
    }
}
