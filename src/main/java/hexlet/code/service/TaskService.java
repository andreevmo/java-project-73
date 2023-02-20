package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.DTO.TaskDTO;
import hexlet.code.model.Task;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Task saveTask(TaskDTO taskDTO);
    Task updateTask(TaskDTO taskDTO, Long id);
    List<Task> getTasks(Predicate predicate, Pageable pageable);
    Task getTask(Long id);
    void deleteTask(Long id);
}
