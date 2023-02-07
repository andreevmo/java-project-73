package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.TaskDTO;
import hexlet.code.app.domain.model.Task;

import java.util.List;

public interface TaskService {

    Task saveTask(TaskDTO taskDTO);
    Task updateTask(TaskDTO taskDTO, Long id);
    List<Task> getAll();
    Task getTask(Long id);
    void deleteTask(Long id);
}
