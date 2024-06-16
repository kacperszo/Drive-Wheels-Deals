package pl.drivewheelsdeals.app.response;

import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

public class RegisterRequest {
    @NotBlank(message = "Country is required")

    public String country;
    @NotBlank(message = "Email is required")
    public String email;
    @NotBlank(message = "Password is required")

    public String password;
    @NotBlank(message = "City is required")

    public String city;
    @NotBlank(message = "Street is required")

    public String street;
    @NotBlank(message = "Zip code is required")
    public String zipCode;
    @NotBlank(message = "Phone is required")
    public String phone;
    @NotBlank(message = "Firstname is required")
    public String firstName;
    @NotBlank(message = "lastname is required")

    public String lastName;
}
