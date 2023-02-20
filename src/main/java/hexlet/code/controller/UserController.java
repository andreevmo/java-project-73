package hexlet.code.controller;

import hexlet.code.DTO.UserDTO;
import hexlet.code.model.User;
import hexlet.code.service.UserServiceImpl;
import hexlet.code.utils.Description;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "${base-url}" + UserController.USER_CONTROLLER_PATH)
@Tag(name = "Пользователи", description = "Работа с пользователями")
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";
    private static final String AUTHORIZE_CONDITION =
            "@userRepository.findById(#id).get().getEmail() == authentication.getName()";
    public static final String ID = "/{id}";
    @Autowired
    private UserServiceImpl userServiceImpl;

    @Operation(summary = Description.GET)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class)) }),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content)
    })
    @GetMapping(path = ID)
    public User getUser(@PathVariable long id) {
        return userServiceImpl.getUser(id);
    }

    @Operation(summary = Description.GET)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS, content = @Content)
    })
    @GetMapping
    public List<User> getUsers() {
        return userServiceImpl.getUsers();
    }


    @Operation(summary = Description.POST)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class)) }),
        @ApiResponse(responseCode = "422", description = Description.UNPROCESSABLE_ENTITY, content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Valid UserDTO userDTO) {
        return userServiceImpl.saveUser(userDTO);
    }

    @Operation(summary = Description.PUT)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class)) }),
        @ApiResponse(responseCode = "422", description = Description.UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content),
        @ApiResponse(responseCode = "403", description = Description.ACCESS_DENIED, content = @Content)
    })
    @PutMapping(path = ID)
    @PreAuthorize(AUTHORIZE_CONDITION)
    public User updateUser(@PathVariable long id, @RequestBody @Valid UserDTO userDTO) {
        return userServiceImpl.updateUser(id, userDTO);

    }

    @Operation(summary = Description.DELETE)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS, content = @Content),
        @ApiResponse(responseCode = "403", description = Description.ACCESS_DENIED, content = @Content),
        @ApiResponse(responseCode = "422", description = Description.UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @DeleteMapping(path = ID)
    @PreAuthorize(AUTHORIZE_CONDITION)
    public void deleteUser(@PathVariable long id) {
        userServiceImpl.deleteUser(id);
    }
}
