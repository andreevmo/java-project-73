package hexlet.code.app.controller;

import hexlet.code.app.domain.DTO.UserDTO;
import hexlet.code.app.domain.model.User;
import hexlet.code.app.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static hexlet.code.app.controller.UserController.USER_CONTROLLER_PATH;

@RestController
@RequestMapping(path = "${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";
    @Autowired
    private UserServiceImpl userServiceImpl;
    @GetMapping(path = ID)
    public User getUser(@PathVariable long id) {
        return userServiceImpl.getUser(id);
    }
    @GetMapping
    public List<User> getUsers() {
        return userServiceImpl.getAll();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid UserDTO userDTO) {
        return userServiceImpl.saveUser(userDTO);
    }

    @PutMapping(path = ID)
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.getName()")
    public User updateUser(@PathVariable long id, @RequestBody @Valid UserDTO userDTO) {
        return userServiceImpl.updateUser(id, userDTO);

    }

    @DeleteMapping(path = ID)
    @PreAuthorize("@userRepository.findById(#id).get().getEmail() == authentication.getName()")
    public void deleteUser(@PathVariable long id) {
        userServiceImpl.deleteUser(id);
    }
}
