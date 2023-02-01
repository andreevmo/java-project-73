package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.UserDTO;
import hexlet.code.app.domain.model.User;
import hexlet.code.app.exception.AuthenticationException;
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
public class UserServiceImpl implements UserService {

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
        try {
            return getUserDTO(userRepository.save(user));
        } catch (Exception e) {
            throw new IncorrectInputException("Input incorrect");
        }
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

    public List<UserDTO> getAll() {
        Iterable<User> users = userRepository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(this::getUserDTO)
                .toList();
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with this id was not found"));
        return getUserDTO(user);
    }

    public void deleteUser(Long id) throws AuthenticationException {
        User userFromDatabase = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with this id was not found"));
        userRepository.deleteById(id);
    }

    public UserDTO getUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getPassword()
        );
    }
}
