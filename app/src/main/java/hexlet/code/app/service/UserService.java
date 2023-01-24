package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.UserDTO;
import hexlet.code.app.domain.model.User;
import hexlet.code.app.exception.IncorrectInputException;
import hexlet.code.app.exception.UserNotFoundException;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public final class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO saveUser(User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectInputException("Input incorrect");
        }
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return getUserDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectInputException("Input incorrect");
        }
        User userFromDatabase = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with this id was not found"));

        user.setId(id);
        user.setCreatedAt(userFromDatabase.getCreatedAt());
        return saveUser(user, bindingResult);
    }

    public List<UserDTO> getUsers() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(user -> getUserDTO(user))
                .toList();
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with this id was not found"));
        return getUserDTO(user);
    }

    public void deleteUser(Long id) {
        User userFromDatabase = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with this id was not found"));
        userRepository.deleteById(id);
    }

    public UserDTO getUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setCreatedAt(user.getCreatedAt());
        return userDTO;
    }
}
