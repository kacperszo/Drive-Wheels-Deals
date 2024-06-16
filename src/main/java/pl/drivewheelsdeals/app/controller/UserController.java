package pl.drivewheelsdeals.app.controller;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.drivewheelsdeals.app.model.Customer;
import pl.drivewheelsdeals.app.model.User;
import pl.drivewheelsdeals.app.request.EditProfileRequest;
import pl.drivewheelsdeals.app.request.RegisterRequest;
import pl.drivewheelsdeals.app.response.EditProfileResponse;
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

    @PostMapping("/user/profile")
    public ResponseEntity<EditProfileResponse> editProfile(@Valid @RequestBody EditProfileRequest request) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }

        User user = (User) auth.getPrincipal();

        if (user == null) {
            throw new BadRequestException("User not found in the authentication context");
        }

        if (userService.isAdministrator(user)) {
            throw new BadRequestException("As Admin you cannot edit your customer profile");
        }

        Customer customer = (Customer) user;
        customer.setLastName(request.lastName);
        customer.setFirstName(request.firstName);
        customer.setCountry(request.country);
        customer.setPhone(request.phone);
        customer.setCity(request.city);
        customer.setStreet(request.street);
        customer.setZipCode(request.zipCode);

        customer = userService.updateCustomer(customer);

        return ResponseEntity.ok(new EditProfileResponse(customer));
    }

    @GetMapping("/user/profile")
    public ResponseEntity<EditProfileResponse> seeProfile() throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        User user = (User) auth.getPrincipal();
        if (user == null) {
            throw new BadRequestException("User not found in the authentication context");
        }
        if (userService.isAdministrator(user)) {
            throw new BadRequestException("As Admin you cannot edit your customer profile");
        }
        Customer customer = (Customer) user;
        return ResponseEntity.ok(new EditProfileResponse(customer));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
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
