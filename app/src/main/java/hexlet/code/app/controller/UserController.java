package hexlet.code.app.controller;

import hexlet.code.app.domain.DTO.UserDTO;
import hexlet.code.app.domain.model.User;
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

@RestController
@RequestMapping(path = "/users")
public final class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/{id}")
    public UserDTO getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @GetMapping(path = "")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(path = "")
    public UserDTO createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.saveUser(user, bindingResult);
    }

    @PutMapping(path = "/{id}")
    public UserDTO updateUser(@PathVariable long id, @Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.updateUser(id, user, bindingResult);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
