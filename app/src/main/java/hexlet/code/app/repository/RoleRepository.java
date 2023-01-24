package hexlet.code.app.repository;

import hexlet.code.app.domain.model.ERole;
import hexlet.code.app.domain.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}
