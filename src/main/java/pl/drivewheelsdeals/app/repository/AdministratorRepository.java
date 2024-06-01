package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import pl.drivewheelsdeals.app.model.Administrator;

public interface AdministratorRepository extends CrudRepository<Administrator, Long> {
}

