package pl.drivewheelsdeals.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.drivewheelsdeals.app.model.Product;

public interface ProductRepository  extends PagingAndSortingRepository<Product, Long> {
}
