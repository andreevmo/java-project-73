package hexlet.code.app.repository;

import hexlet.code.app.domain.model.QTask;
import hexlet.code.app.domain.model.Task;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>,
        QuerydslPredicateExecutor<Task>,
        QuerydslBinderCustomizer<QTask> {
    boolean existsByName(String name);

    @Override
    default void customize(QuerydslBindings bindings, QTask root) {

    }
}
