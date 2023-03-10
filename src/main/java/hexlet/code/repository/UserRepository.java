package hexlet.code.repository;

import hexlet.code.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
