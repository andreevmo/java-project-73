package hexlet.code.app.repository;

import hexlet.code.app.domain.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    boolean existsByName(String name);
}
