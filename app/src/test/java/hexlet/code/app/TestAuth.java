package hexlet.code.app;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static hexlet.code.app.TestUtils.TEST_PATH;
import static hexlet.code.app.TestUtils.performRequest;
import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.app.controller.StatusController.STATUS_CONTROLLER_PATH;
import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class TestAuth {

    @Autowired
    private MockMvc mockMvc;

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
}
