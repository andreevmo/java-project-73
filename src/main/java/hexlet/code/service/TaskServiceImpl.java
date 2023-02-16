package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.domain.DTO.TaskDTO;
import hexlet.code.domain.model.Label;
import hexlet.code.domain.model.Status;
import hexlet.code.domain.model.Task;
import hexlet.code.domain.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    public List<Task> getAll(Predicate predicate, Pageable pageable) {
        return (List<Task>) taskRepository.findAll(predicate);
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
