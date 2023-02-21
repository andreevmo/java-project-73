package hexlet.code;

import hexlet.code.config.jwt.JwtUtils;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BODY_FOR_TEST_LOGIN;
import static hexlet.code.utils.TestUtils.BODY_FOR_TEST_TASK;
import static hexlet.code.utils.TestUtils.BODY_FOR_TEST_USERS;
import static hexlet.code.utils.TestUtils.TEST_PATH;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class TestAuth {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TestUtils testUtils;

    public static final String LOGIN_CONTROLLER_PATH = TEST_PATH + "/login";

    @ParameterizedTest
    @ValueSource(strings = {
        USER_CONTROLLER_PATH,
        LABEL_CONTROLLER_PATH,
        STATUS_CONTROLLER_PATH,
        TASK_CONTROLLER_PATH
    })
    void testRequestWithoutAuth(String controllerPath) throws Exception {
        if (!controllerPath.equals(USER_CONTROLLER_PATH)) {
            testUtils.performRequest(
                    mockMvc, TEST_PATH + controllerPath, HttpMethod.GET, status().isUnauthorized());
            testUtils.performRequest(
                    mockMvc, TEST_PATH + controllerPath, HttpMethod.POST, status().isUnauthorized());
        }
        testUtils.performRequest(
                mockMvc, TEST_PATH + controllerPath + "/1", HttpMethod.PUT, status().isUnauthorized());
        testUtils.performRequest(
                mockMvc, TEST_PATH + controllerPath + "/1", HttpMethod.DELETE, status().isUnauthorized());
    }

    @Test
    @Sql(value = {"/importSQL/importUsers.sql"})
    void testLogin() throws Exception {
        testUtils.performRequest(mockMvc, LOGIN_CONTROLLER_PATH, HttpMethod.POST, status().isOk(),
                BODY_FOR_TEST_LOGIN);
        String incorrectData = BODY_FOR_TEST_LOGIN.replace("ivan@google.com", "max_525@mail.ru");
        testUtils.performRequest(
                mockMvc, LOGIN_CONTROLLER_PATH, HttpMethod.POST, status().isUnauthorized(),
                incorrectData);
    }

    @Test
    @Sql(value = {"/importSQL/importUsers.sql", "/importSQL/importStatuses.sql"})
    void testRequestWithToken() throws Exception {
        testUtils.performRequest(mockMvc, TEST_PATH + USER_CONTROLLER_PATH, HttpMethod.POST, status().isCreated(),
                BODY_FOR_TEST_USERS);
        String token = jwtUtils.generateJwtToken("ivan@google.com");
        mockMvc.perform(MockMvcRequestBuilders.post(TEST_PATH + TASK_CONTROLLER_PATH)
              .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_FOR_TEST_TASK))
              .andExpect(status().isCreated());
    }
}
