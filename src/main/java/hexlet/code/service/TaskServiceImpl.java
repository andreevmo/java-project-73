package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.DTO.TaskDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Status;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;
    private UserService userService;
    private LabelService labelService;
    private StatusService statusService;
    @Override
    public Task saveTask(TaskDTO taskDTO) {
        Task task = createTask(taskDTO);
        System.out.println(task);
        task = taskRepository.save(task);
        System.out.println(task);
        return task;
    }

    @Override
    @Transactional
    public Task updateTask(TaskDTO taskDTO, Long id) {
        Task taskFromDB = taskRepository.findById(id).orElseThrow();
        Task task = createTask(taskDTO);
        taskFromDB.setTaskStatus(task.getTaskStatus());
        taskFromDB.setAuthor(task.getAuthor());
        taskFromDB.setName(task.getName());
        taskFromDB.setLabels(task.getLabels());
        taskFromDB.setExecutor(task.getExecutor());
        taskFromDB.setDescription(task.getDescription());
        return taskFromDB;
    }

    @Override
    public List<Task> getTasks(Predicate predicate, Pageable pageable) {
        return (List<Task>) taskRepository.findAll(predicate);
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private Task createTask(final TaskDTO taskDTO) {
        final User author = userService.getCurrentUser();
        final User executor = userService.getUser(taskDTO.getExecutorId());
        final Status status = statusService.getStatus(taskDTO.getTaskStatusId());
        final Set<Label> labels = Optional.ofNullable(taskDTO.getLabelIds())
                .orElse(Set.of())
                .stream()
                .filter(Objects::nonNull)
                .map(id -> labelService.getLabel(id))
                .collect(Collectors.toSet());

        return Task.builder()
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
                .taskStatus(status)
                .labels(labels)
                .author(author)
                .executor(executor)
                .build();
    }
}
