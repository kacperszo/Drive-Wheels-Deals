package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import pl.drivewheelsdeals.app.model.Product;

public interface ProductRepository  extends CrudRepository<Product, Long> {
}
