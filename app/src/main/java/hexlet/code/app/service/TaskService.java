package hexlet.code.app.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.app.domain.DTO.TaskDTO;
import hexlet.code.app.domain.model.Task;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Task saveTask(TaskDTO taskDTO);
    Task updateTask(TaskDTO taskDTO, Long id);
    List<Task> getAll(Predicate predicate, Pageable pageable);
    Task getTask(Long id);
    void deleteTask(Long id);
}
