package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.TaskDTO;
import hexlet.code.app.domain.model.Label;
import hexlet.code.app.domain.model.Status;
import hexlet.code.app.domain.model.Task;
import hexlet.code.app.domain.model.User;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Override
    public Task saveTask(TaskDTO taskDTO) {
        Task task = createTask(taskDTO);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(TaskDTO taskDTO, Long id) {
        Task taskFromDB = taskRepository.findById(id).orElseThrow();
        Task task = createTask(taskDTO);
        task.setId(id);
        task.setCreatedAt(taskFromDB.getCreatedAt());
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAll() {
        return (List<Task>) taskRepository.findAll();
    }

    @Override
    public Task getTask(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        taskRepository.deleteById(id);
    }

    private Task createTask(TaskDTO taskDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User author = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        User executor = taskDTO.getExecutorId() == null ? null
                : userRepository.findById(taskDTO.getExecutorId()).orElse(null);
        Status status = statusRepository.findById(taskDTO.getTaskStatusId()).orElseThrow();
        List<Label> labelList = taskDTO.getLabelIds() == null ? new ArrayList<>()
                : Arrays.stream(taskDTO.getLabelIds())
                .map(id -> labelRepository.findById(id).orElseThrow())
                .toList();
        return new Task(
                taskDTO.getName(),
                taskDTO.getDescription(),
                status,
                author,
                executor,
                labelList
        );
    }
}
