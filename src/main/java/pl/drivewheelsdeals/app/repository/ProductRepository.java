package pl.drivewheelsdeals.app.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.drivewheelsdeals.app.model.Product;

import java.util.List;

public interface ProductRepository  extends PagingAndSortingRepository<Product, Long>, CrudRepository<Product, Long> {
    Product findProductById(Long id);

    @Override
    List<Product> findAllById(Iterable<Long> ids);

}
