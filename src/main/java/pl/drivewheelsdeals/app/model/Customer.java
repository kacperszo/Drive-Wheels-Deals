package pl.drivewheelsdeals.app.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Customer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String phone;
    private String zipCode;
    private String city;
    private String street;
    private String country;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Product> basket;


    public Customer() {
    }
    
    public Customer(String firstName, String lastName, String email, String password, String phone, String zipCode, String city, String street, String country) {
        super(firstName, lastName, email, password);
        this.phone = phone;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.country = country;
    }

    public List<Product> getBasket() {
        return basket;
    }

    public void setBasket(List<Product> basket) {
        this.basket = basket;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
