package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import pl.drivewheelsdeals.app.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
