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
    public List<Product> showMyBasket() throws BadRequestException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (userService.isAdministrator(user)) {
            throw new BadRequestException("As Admin you don't have a basket to show");
        }

        Customer customer = (Customer) user;

        return customer.getBasket();
    }


    @PostMapping("/basket/add/{id}")
    public List<Product> addToBasket(@PathVariable Long id) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        Product product = productService.getById(id);
        if (product == null) {
            throw new BadRequestException("Product of given id not found");
        }

        if (userService.isAdministrator(user)) {
            throw new BadRequestException("As Admin you cannot edit your basket");
        }

        Customer customer = (Customer) user;
        //check if product is in stock
        List<Product> basket = customer.getBasket();
        long inBasket = basket.stream().filter(p->p.getId().equals(id)).count();
        boolean inStock = product.getQuantityInStock() - (inBasket + 1) >= 0;

        if(inStock){
            basket.add(product);
        }else{
            throw new BadRequestException("Not enough products in stock");
        }

        return userService.updateCustomer(customer).getBasket();
    }

    @DeleteMapping("/basket/remove/{id}")
    public List<Product> removeFromBasket(@PathVariable Long id) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        Product product = productService.getById(id);
        if (product == null) {
            throw new BadRequestException("Product of given id not found");
        }
        if (userService.isAdministrator(user)) {
            throw new BadRequestException("As Admin you cannot edit your basket");
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
