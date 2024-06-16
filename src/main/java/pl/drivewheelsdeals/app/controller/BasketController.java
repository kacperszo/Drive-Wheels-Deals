package pl.drivewheelsdeals.app.controller;


import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.drivewheelsdeals.app.model.Customer;
import pl.drivewheelsdeals.app.model.Product;
import pl.drivewheelsdeals.app.model.User;
import pl.drivewheelsdeals.app.service.ProductService;
import pl.drivewheelsdeals.app.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BasketController {

    private final UserService userService;
    private final ProductService productService;

    public BasketController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/basket")
    public List<Product> getBasket() throws BadRequestException {
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

        return customer.getBasket();
    }


    @PostMapping("/basket/add/{id}")
    public List<Product> addToBasket(@PathVariable Long id) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        User user = (User) auth.getPrincipal();
        if (user == null) {
            throw new BadRequestException("User not found in the authentication context");
        }
        Product product = productService.getById(id);
        if (product == null) {
            throw new BadRequestException("Product of given id not found");
        }
        if (userService.isAdministrator(user)) {
            throw new BadRequestException("As Admin you cannot edit your customer profile");
        }
        Customer customer = (Customer) user;


        customer.getBasket().add(product);

        return userService.updateCustomer(customer).getBasket();
    }

    @DeleteMapping("/basket/remove/{id}")
    public List<Product> removeFromBasket(@PathVariable Long id) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        User user = (User) auth.getPrincipal();
        if (user == null) {
            throw new BadRequestException("User not found in the authentication context");
        }
        Product product = productService.getById(id);
        if (product == null) {
            throw new BadRequestException("Product of given id not found");
        }
        if (userService.isAdministrator(user)) {
            throw new BadRequestException("As Admin you cannot edit your customer profile");
        }
        Customer customer = (Customer) user;


        customer.getBasket().remove(product);

        return userService.updateCustomer(customer).getBasket();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, String> handleGeneralBadRequestExceptions(BadRequestException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return errors;
    }
}