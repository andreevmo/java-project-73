package hexlet.code.app.controller;

import hexlet.code.app.domain.DTO.TaskDTO;
import hexlet.code.app.domain.model.Task;
import hexlet.code.app.service.TaskServiceImpl;
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

@RestController
@RequestMapping(path = "${base-url}" + TaskController.TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";

    @Autowired
    private TaskServiceImpl taskService;

    @GetMapping(path = ID)
    public Task getUser(@PathVariable long id) {
        return taskService.getTask(id);
    }

    @GetMapping
    public List<Task> getUsers() {
        return taskService.getAll();
    }

    @PostMapping
    public Task createUser(@RequestBody @Valid TaskDTO taskDTO) {
        return taskService.saveTask(taskDTO);
    }

    @PutMapping(path = ID)
    public Task updateUser(@PathVariable long id, @RequestBody @Valid TaskDTO taskDTO) {
        return taskService.updateTask(taskDTO, id);

    }

    @DeleteMapping(path = ID)
    @PreAuthorize("@taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()")
    public void deleteUser(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
