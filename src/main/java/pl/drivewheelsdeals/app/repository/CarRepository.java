package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import pl.drivewheelsdeals.app.model.Car;

public interface CarRepository extends CrudRepository<Car, Long> {
}
