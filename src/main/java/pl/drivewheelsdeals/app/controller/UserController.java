package pl.drivewheelsdeals.app.controller;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.drivewheelsdeals.app.model.Customer;
import pl.drivewheelsdeals.app.response.RegisterRequest;
import pl.drivewheelsdeals.app.response.RegisterResponse;
import pl.drivewheelsdeals.app.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public RegisterResponse registerUser(@Valid @RequestBody RegisterRequest request) throws BadRequestException {
        try {
            Customer customer = new Customer();
            customer.setEmail(request.email);
            customer.setPassword(passwordEncoder.encode(request.password));
            customer.setCity(request.city);
            customer.setCountry(request.country);
            customer.setStreet(request.street);
            customer.setZipCode(request.zipCode);
            customer.setPhone(request.phone);
            customer.setFirstName(request.firstName);
            customer.setLastName(request.lastName);

            return new RegisterResponse(userService.createCustomer(customer));

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, String> handleGeneralBadRequestExceptions(BadRequestException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }
}
