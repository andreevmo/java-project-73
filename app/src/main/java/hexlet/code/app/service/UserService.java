package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.UserDTO;
import hexlet.code.app.domain.model.User;

import javax.security.sasl.AuthenticationException;
import java.util.List;

public interface UserService {

    User saveUser(UserDTO userDTO);
    User updateUser(Long id, UserDTO userDTO);
    List<User> getAll();
    User getUser(Long id);
    void deleteUser(Long id) throws AuthenticationException, org.springframework.security.core.AuthenticationException;
}
