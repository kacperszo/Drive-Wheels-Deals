package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import pl.drivewheelsdeals.app.model.OrderItem;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
