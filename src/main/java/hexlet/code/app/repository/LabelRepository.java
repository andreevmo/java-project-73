package hexlet.code.app.repository;

import hexlet.code.app.domain.model.Label;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends CrudRepository<Label, Long> {
}
