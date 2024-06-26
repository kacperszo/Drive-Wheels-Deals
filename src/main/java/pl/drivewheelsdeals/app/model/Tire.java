package pl.drivewheelsdeals.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Tire extends Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    String brand;
    String size;

    public Tire() {
    }

    public Tire(String brand, String size, BigDecimal price,int quantityInStock) {
        super(price, quantityInStock);
        this.brand = brand;
        this.size = size;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tire tire = (Tire) o;
        return Objects.equals(id, tire.id) && Objects.equals(brand, tire.brand) && Objects.equals(size, tire.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, brand, size);
    }
}
