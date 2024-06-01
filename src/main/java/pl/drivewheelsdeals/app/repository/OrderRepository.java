package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import pl.drivewheelsdeals.app.model.Order;

public interface OrderRepository  extends CrudRepository<Order, Long> {
}
