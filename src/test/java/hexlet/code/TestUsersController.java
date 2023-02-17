package hexlet.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.domain.model.User;
import hexlet.code.repository.UserRepository;
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

import static hexlet.code.utils.TestUtils.BODY_FOR_TEST_USERS;
import static hexlet.code.utils.TestUtils.TEST_PATH;
import static hexlet.code.utils.TestUtils.performRequest;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@WithMockUser(username = "ivan@google.com")
@Sql(value = {"/importSQL/importUsers.sql"})
class TestUsersController {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper mapper;
    private static List<User> usersForTest;
    private final String baseUrl = TEST_PATH + USER_CONTROLLER_PATH;

    @BeforeEach
    void beforeEach() {
        usersForTest = (List<User>) userRepository.findAll();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetUserPositive() throws Exception {
        String answer = performRequest(mockMvc, baseUrl + "/2", HttpMethod.GET, status().isOk());
        assertThat(answer).isEqualTo(mapper.writeValueAsString(usersForTest.get(1)));
    }

    @Test
    void testGetUserNegative() throws Exception {
        performRequest(mockMvc, baseUrl + "/10", HttpMethod.GET, status().isNotFound());
    }

    @Test
    void testGetUsers() throws Exception {
        String answer = performRequest(mockMvc, baseUrl, HttpMethod.GET, status().isOk());
        assertThat(answer).isEqualTo(mapper.writeValueAsString(usersForTest));
    }

    @Test
    void testPostUser() throws Exception {
        String answer = performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isCreated(), BODY_FOR_TEST_USERS);
        User user = userRepository.findById(3L).orElseThrow();
        assertThat(user.getEmail()).isEqualTo("maxim_525@mail.ru");
        assertThat(answer).isEqualTo(mapper.writeValueAsString(user));
    }

    @Test
    void testPostUserNegative() throws Exception {
        String incorrectBody = BODY_FOR_TEST_USERS.replace("\"some-password1\"", "null");
        performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testPutUser() throws Exception {
        String oldEmail = usersForTest.get(0).getEmail();
        String answer = performRequest(
                mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isOk(), BODY_FOR_TEST_USERS);
        User user = userRepository.findById(1L).orElseThrow();
        assertThat(user.getEmail()).isNotEqualTo(oldEmail).isEqualTo("maxim_525@mail.ru");
        assertThat(answer).isEqualTo(mapper.writeValueAsString(user));
    }

    @Test
    void testPutUserNegative() throws Exception {
        String incorrectBody = BODY_FOR_TEST_USERS.replace("Andreev", "");
        performRequest(mockMvc, baseUrl + "/1", HttpMethod.PUT, status().isUnprocessableEntity(), incorrectBody);
    }

    @Test
    void testDeleteUser() throws Exception {
        performRequest(mockMvc, baseUrl + "/1", HttpMethod.DELETE, status().isOk());
        assertThat(userRepository.existsById(1L)).isFalse();
    }
    @Test
    void testDeleteUserNegative() throws Exception {
        performRequest(mockMvc, baseUrl + "/2", HttpMethod.DELETE, status().isForbidden());
        performRequest(mockMvc, baseUrl + "/4", HttpMethod.DELETE, status().isNotFound());
        assertThat(userRepository.existsById(2L)).isTrue();
    }

    @Test
    void testUniqueEmail() throws Exception {
        performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isCreated(), BODY_FOR_TEST_USERS);
        performRequest(mockMvc, baseUrl, HttpMethod.POST, status().isUnprocessableEntity(), BODY_FOR_TEST_USERS);
    }
}
