package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import pl.drivewheelsdeals.app.model.Tire;

public interface TireRepository  extends CrudRepository<Tire, Long> {
}
