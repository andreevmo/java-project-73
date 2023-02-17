package hexlet.code.service;

import hexlet.code.domain.DTO.UserDTO;
import hexlet.code.domain.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(UserDTO userDTO) {
        User user = createUser(userDTO);
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserDTO userDTO) {
        User user = createUser(userDTO);
        User userFromDatabase = userRepository.findById(id).orElseThrow();
        user.setId(id);
        user.setCreatedAt(userFromDatabase.getCreatedAt());
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return (List<User>) userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        userRepository.deleteById(id);
    }

    private User createUser(UserDTO userDTO) {
        return new User(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword())
        );
    }
}
