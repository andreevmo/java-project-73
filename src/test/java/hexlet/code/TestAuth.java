package hexlet.code;

import hexlet.code.config.jwt.JwtUtils;
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

import static hexlet.code.TestUtils.BODY_FOR_TEST_LOGIN;
import static hexlet.code.TestUtils.BODY_FOR_TEST_TASK;
import static hexlet.code.TestUtils.BODY_FOR_TEST_USERS;
import static hexlet.code.TestUtils.TEST_PATH;
import static hexlet.code.TestUtils.performRequest;
import static hexlet.code.controller.AuthController.LOGIN_CONTROLLER_PATH;
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
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

    @ParameterizedTest
    @ValueSource(strings = {
        USER_CONTROLLER_PATH,
        LABEL_CONTROLLER_PATH,
        STATUS_CONTROLLER_PATH,
        TASK_CONTROLLER_PATH
    })
    void testRequestWithoutAuth(String controllerPath) throws Exception {
        if (!controllerPath.equals(USER_CONTROLLER_PATH)) {
            performRequest(mockMvc, TEST_PATH + controllerPath, HttpMethod.GET, status().isUnauthorized());
            performRequest(mockMvc, TEST_PATH + controllerPath, HttpMethod.POST, status().isUnauthorized());
        }
        performRequest(mockMvc, TEST_PATH + controllerPath + "/1", HttpMethod.PUT, status().isUnauthorized());
        performRequest(mockMvc, TEST_PATH + controllerPath + "/1", HttpMethod.DELETE, status().isUnauthorized());
    }

    @Test
    @Sql(value = {"/importSQL/importUsers.sql"})
    void testLogin() throws Exception {
        performRequest(mockMvc, TEST_PATH + LOGIN_CONTROLLER_PATH, HttpMethod.POST, status().isOk(),
                BODY_FOR_TEST_LOGIN);
        String incorrectData = BODY_FOR_TEST_LOGIN.replace("ivan@google.com", "max_525@mail.ru");
        performRequest(
                mockMvc, TEST_PATH + LOGIN_CONTROLLER_PATH, HttpMethod.POST, status().isUnauthorized(),
                incorrectData);
    }

    @Test
    @Sql(value = {"/importSQL/importUsers.sql"})
    void testRequestWithToken() throws Exception {
        performRequest(mockMvc, TEST_PATH + USER_CONTROLLER_PATH, HttpMethod.POST, status().isOk(),
                BODY_FOR_TEST_USERS);
        String token = jwtUtils.generateJwtToken("ivan@google.com");
        String incorrectToken = jwtUtils.generateJwtToken("ivan@google.ru");
        mockMvc.perform(MockMvcRequestBuilders.post(TEST_PATH + TASK_CONTROLLER_PATH)
              .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_FOR_TEST_TASK))
              .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post(TEST_PATH + TASK_CONTROLLER_PATH)
                        .header("Authorization", "Bearer " + incorrectToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(BODY_FOR_TEST_TASK))
                .andExpect(status().isUnauthorized());
    }
}
