package pl.drivewheelsdeals.app.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class ProductOperationRequest {
    @NotBlank(message = "Product type is required")
    public String productType;
    public BigDecimal price;

    // Optional - used in removing
    public Long id;

    // Optional
    public String brand;
    // Optional - car
    public String model;
    public Integer year;
    // Optional - tire
    public String size;

    public ProductOperationRequest(BigDecimal price, String productType){
        this.price = price;
        this.productType = productType;
    }
}
