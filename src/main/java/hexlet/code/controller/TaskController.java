package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.domain.DTO.TaskDTO;
import hexlet.code.domain.model.Task;
import hexlet.code.service.TaskServiceImpl;
import hexlet.code.utils.Description;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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
@RequestMapping(path = "${base-url}" + TaskController.TASK_CONTROLLER_PATH)
@Tag(name = "Задачи", description = "Работа с задачами")
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";
    private static final String AUTHORIZE_CONDITION =
            "@taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()";
    @Autowired
    private TaskServiceImpl taskService;

    @Operation(summary = Description.GET)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Task.class)) }),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED,
                content = @Content)
    })
    @GetMapping(path = ID)
    public Task getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    @Operation(summary = Description.GET)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @GetMapping
    public List<Task> getTasks(@Parameter(hidden = true) @QuerydslPredicate(root = Task.class) Predicate predicate,
                               @Parameter(hidden = true) Pageable pageable) {
        return taskService.getAll(predicate, pageable);
    }

    @Operation(summary = Description.POST)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description =  Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Task.class)) }),
        @ApiResponse(responseCode = "422", description = Description.UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@RequestBody @Valid TaskDTO taskDTO) {
        return taskService.saveTask(taskDTO);
    }

    @Operation(summary = Description.PUT)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Task.class)) }),
        @ApiResponse(responseCode = "422", description = Description.UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @PutMapping(path = ID)
    public Task updateTask(@PathVariable long id, @RequestBody @Valid TaskDTO taskDTO) {
        return taskService.updateTask(taskDTO, id);

    }

    @DeleteMapping(path = ID)
    @Operation(summary = Description.DELETE)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS, content = @Content),
        @ApiResponse(responseCode = "403", description = Description.ACCESS_DENIED, content = @Content),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @PreAuthorize(AUTHORIZE_CONDITION)
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
