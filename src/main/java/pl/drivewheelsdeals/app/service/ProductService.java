package pl.drivewheelsdeals.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.drivewheelsdeals.app.model.Product;
import pl.drivewheelsdeals.app.repository.CarRepository;
import pl.drivewheelsdeals.app.repository.ProductRepository;
import pl.drivewheelsdeals.app.repository.TireRepository;

@Service
public class ProductService {

    private final TireRepository tireRepository;
    private final CarRepository carRepository;
    private final ProductRepository productRepository;

    public ProductService(TireRepository tireRepository, CarRepository carRepository, ProductRepository productRepository) {
        this.tireRepository = tireRepository;
        this.carRepository = carRepository;
        this.productRepository = productRepository;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getById(Long id) {
        return productRepository.findProductById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        productRepository.delete(product);
    }
}
