package pl.drivewheelsdeals.app.service;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.drivewheelsdeals.app.model.Car;
import pl.drivewheelsdeals.app.model.Product;
import pl.drivewheelsdeals.app.model.Tire;
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

    public Product createProduct(Product product){
        if(product instanceof Car){
            return carRepository.save((Car) product);
        } else {
            return tireRepository.save((Tire) product);
        }
    }

    public Product updateProduct(Product product) throws BadRequestException {
        if(product instanceof Car){
            if(carRepository.findById(product.getId()).isEmpty()){
                throw new BadRequestException("Car with this id does not exist");
            } else {
                return carRepository.save((Car) product);
            }
        } else {
            if(tireRepository.findById(product.getId()).isEmpty()){
                throw new BadRequestException("Tire with this id does not exist");
            } else {
                return tireRepository.save((Tire) product);
            }
        }
    }

    public void removeProduct(Product product) throws BadRequestException {
        if(product instanceof Car) {
            if(carRepository.findById(product.getId()).isEmpty()){
                throw new BadRequestException("Car with this id does not exist");
            } else {
                carRepository.deleteById(product.getId());
            }
        } else {
            if(tireRepository.findById(product.getId()).isEmpty()){
                throw new BadRequestException("Tire with this id does not exist");
            } else {
                tireRepository.deleteById(product.getId());
            }
        }
    }
}
