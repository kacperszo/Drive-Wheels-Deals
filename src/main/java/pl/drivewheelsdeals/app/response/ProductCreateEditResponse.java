package pl.drivewheelsdeals.app.response;

import pl.drivewheelsdeals.app.model.Car;
import pl.drivewheelsdeals.app.model.Product;
import pl.drivewheelsdeals.app.model.Tire;

import java.math.BigDecimal;

public class ProductCreateEditResponse {

    public Long id;
    public BigDecimal price;
    public String brand;
    // Optional - car
    public String model;
    public Integer year;
    // Optional - tire
    public String size;

    public ProductCreateEditResponse(Product product){
        this.id = product.getId();
        this.price = product.getPrice();
        if(product instanceof Car){
            this.brand = ((Car) product).getBrand();
            this.year = ((Car) product).getYear();
            this.model = ((Car) product).getModel();
        } else {
            this.brand = ((Tire) product).getBrand();
            this.size = ((Tire) product).getSize();
        }
    }
}
