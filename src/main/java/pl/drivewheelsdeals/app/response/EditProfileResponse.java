package pl.drivewheelsdeals.app.response;

import pl.drivewheelsdeals.app.model.Customer;

public class EditProfileResponse {
    public String country;
    public String city;
    public String street;
    public String zipCode;
    public String phone;
    public String firstName;
    public String lastName;

    public EditProfileResponse(Customer customer) {
        this.country = customer.getCountry();
        this.city = customer.getCity();
        this.street = customer.getStreet();
        this.zipCode = customer.getZipCode();
        this.phone = customer.getPhone();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
    }
}
