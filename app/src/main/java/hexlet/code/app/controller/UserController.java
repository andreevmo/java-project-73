package hexlet.code.app.controller;

import hexlet.code.app.DTO.UserDTO;
import hexlet.code.app.exception.IncorrectInputException;
import hexlet.code.app.exception.UserNotFoundException;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(path = "/users")
public final class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{id}")
    public UserDTO getUser(@PathVariable long id) {
        User user = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with this id was not found"));
        return userService.getUserDTO(user);
    }

    @GetMapping(path = "")
    public List<UserDTO> getUsers() {
        Iterable<User> users = repository.findAll();
        return StreamSupport.stream(users.spliterator(), false)
                .map(user -> userService.getUserDTO(user))
                .toList();
    }

    @PostMapping(path = "")
    public UserDTO createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectInputException("Input incorrect");
        }
        return userService.getUserDTO(userService.saveUser(user));
    }

    @PutMapping(path = "/{id}")
    public UserDTO updateUser(@PathVariable long id, @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectInputException("Input incorrect");
        }
        User userFromDatabase = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with this id was not found"));

        user.setId(id);
        user.setCreatedAt(userFromDatabase.getCreatedAt());
        return userService.getUserDTO(userService.saveUser(user));
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable long id) {
        User userFromDatabase = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("User with this id was not found"));
        repository.deleteById(id);
    }
}
