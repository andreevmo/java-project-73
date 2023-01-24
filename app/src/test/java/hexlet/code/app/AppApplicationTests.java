package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.domain.model.User;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WithMockUser
@TestPropertySource("/application-test.properties")
@Sql(value = {"/import.sql"})
class AppApplicationTests {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private static List<User> usersForTest;

    private static Path path = Path.of("src/test/resources/users");
    private static String baseUrl = "http://localhost:5000";

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
        mockMvc.perform(get(baseUrl + "/welcome").with(user("User")))
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
        User firstUser = usersForTest.get(0);
        assertThat(answer).contains(firstUser.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(firstUser.getFirstName());
        assertThat(answer).contains(firstUser.getLastName());
        assertThat(answer).contains("createdAt");

        User secondUser = usersForTest.get(1);
        assertThat(answer).contains(secondUser.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(secondUser.getFirstName());
        assertThat(answer).contains(secondUser.getLastName());
        assertThat(answer).contains("createdAt");
    }

    @Test
    void testPostUser() throws Exception {
        User newUser = new User();
        newUser.setEmail("maxim_525@mail.ru");
        newUser.setFirstName("Maxim");
        newUser.setLastName("Andreev");
        newUser.setPassword("some-password1");
        String userJson = new ObjectMapper().writeValueAsString(newUser);
        MockHttpServletResponse response = mockMvc
                .perform(post(baseUrl + "/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(200);
        String answer = response.getContentAsString();
        assertThat(answer).contains(newUser.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(newUser.getFirstName());
        assertThat(answer).contains(newUser.getLastName());
        assertThat(answer).contains("createdAt");
    }

    @Test
    void testPostUserNegative() throws Exception {
        User newUser = new User();
        newUser.setEmail("ivan@google.com");
        newUser.setFirstName("Ivan");
        newUser.setLastName("Petrov");
        newUser.setPassword("so");
        String userJson = new ObjectMapper().writeValueAsString(newUser);
        mockMvc.perform(post(baseUrl + "/users")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testPutUser() throws Exception {
        User updateUser = new User();
        updateUser.setEmail("maxim_525@mail.ru");
        updateUser.setFirstName("Max");
        updateUser.setLastName("Ivanov");
        updateUser.setPassword("12348765");
        String userJson = new ObjectMapper().writeValueAsString(updateUser);
        String answer = mockMvc
                .perform(put(baseUrl + "/users/1")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(answer).contains(updateUser.getEmail());
        assertThat(answer).doesNotContain("password");
        assertThat(answer).contains(updateUser.getFirstName());
        assertThat(answer).contains(updateUser.getLastName());
        assertThat(answer).contains("createdAt");
    }

    @Test
    void testPutUserNegative() throws Exception {
        User updateUser = new User();
        updateUser.setEmail("maxim_525@mail.ru");
        updateUser.setFirstName("Max");
        updateUser.setLastName("");
        updateUser.setPassword("12348765");
        String userJson = new ObjectMapper().writeValueAsString(updateUser);
        mockMvc.perform(put(baseUrl + "/users/1")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testDeleteUser() throws Exception {
        boolean existsNewUser = userRepository.existsById(1L);
        assertThat(existsNewUser).isTrue();
        mockMvc
                .perform(delete(baseUrl + "/users/1"))
                .andExpect(status().isOk());

        existsNewUser = userRepository.existsById(1L);
        assertThat(existsNewUser).isFalse();
    }
}
