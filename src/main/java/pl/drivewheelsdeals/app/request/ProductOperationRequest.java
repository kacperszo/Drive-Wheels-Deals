package pl.drivewheelsdeals.app.request;

import java.math.BigDecimal;

public class ProductOperationRequest {

    public BigDecimal price;
    public String product_type;

    // Optional - used in removing
    public Long id;

    // Optional - car/tire specific
    public String brand;
    public String model;
    public Integer year;
    public String size;

    public ProductOperationRequest(BigDecimal price, String product_type){
        this.price = price;
        this.product_type = product_type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
