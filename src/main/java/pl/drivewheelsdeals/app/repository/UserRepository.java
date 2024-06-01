package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import pl.drivewheelsdeals.app.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
