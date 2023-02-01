package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.UserDTO;
import hexlet.code.app.domain.model.User;
import org.springframework.validation.BindingResult;

import javax.security.sasl.AuthenticationException;
import java.util.List;

public interface UserService {

    UserDTO saveUser(User user, BindingResult bindingResult);
    UserDTO updateUser(Long id, User user, BindingResult bindingResult);
    List<UserDTO> getAll();
    UserDTO getUser(Long id);
    void deleteUser(Long id) throws AuthenticationException, org.springframework.security.core.AuthenticationException;
    UserDTO getUserDTO(User user);

}
