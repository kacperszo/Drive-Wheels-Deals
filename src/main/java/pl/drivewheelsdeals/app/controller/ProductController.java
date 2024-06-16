package pl.drivewheelsdeals.app.controller;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.drivewheelsdeals.app.model.Car;
import pl.drivewheelsdeals.app.model.Product;
import pl.drivewheelsdeals.app.model.Tire;
import pl.drivewheelsdeals.app.model.User;
import pl.drivewheelsdeals.app.request.ProductOperationRequest;
import pl.drivewheelsdeals.app.response.ProductOperationResponse;
import pl.drivewheelsdeals.app.service.ProductService;
import pl.drivewheelsdeals.app.service.UserService;

@RestController
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/product/{id}")
    public Product listProducts(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/product")
    public Page<Product> listProducts(Integer page, Integer size) {
        if(page == null) page=0;
        if(size == null) size=10;
        PageRequest pageRequest = PageRequest.of(page, size);
        return productService.getAllProducts(pageRequest);
    }

    @PostMapping("/product/add")
    public ProductOperationResponse addProduct(@Valid @RequestBody ProductOperationRequest request) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        User user = (User) auth.getPrincipal();
        if (user == null) {
            throw new BadRequestException("User not found in the authentication context");
        }
        if (!userService.isAdministrator(user)) {
            throw new BadRequestException("User is not an admin");
        }

        if(request.product_type.equals("Car")){
            Car product = new Car();
            product.setBrand(request.brand);
            product.setModel(request.model);
            product.setYear(request.year);
            product.setPrice(request.price);

            Product added = productService.createProduct(product);

            return new ProductOperationResponse("success -> created product id: " + added.getId());

        } else if (request.product_type.equals("Tire")) {
            Tire product = new Tire();
            product.setBrand(request.brand);
            product.setSize(request.size);
            product.setPrice(request.price);

            Product added = productService.createProduct(product);

            return new ProductOperationResponse("success -> created product id: " + added.getId());
        } else {
            throw new BadRequestException("Invalid product type");
        }
    }

    @PostMapping("/product/edit")
    public ProductOperationResponse editProduct(@Valid @RequestBody ProductOperationRequest request) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        User user = (User) auth.getPrincipal();
        if (user == null) {
            throw new BadRequestException("User not found in the authentication context");
        }
        if (!userService.isAdministrator(user)) {
            throw new BadRequestException("User is not an admin");
        }

        if(request.product_type.equals("Car")){
            Car product = (Car) productService.getById(request.id);
            product.setYear(request.year);
            product.setPrice(request.price);
            product.setModel(request.model);
            product.setBrand(request.brand);

            product = (Car) productService.updateProduct(product);

            return new ProductOperationResponse("success -> edited product id: " + product.getId());

        } else if (request.product_type.equals("Tire")) {
            Tire product = (Tire) productService.getById(request.id);
            product.setPrice(request.price);
            product.setBrand(request.brand);

            product = (Tire) productService.updateProduct(product);

            return new ProductOperationResponse("success -> edited product id: " + product.getId());

        } else {
            throw new BadRequestException("Invalid product type");
        }
    }

    @PostMapping("/product/delete")
    public ProductOperationResponse deleteProduct(@Valid @RequestBody ProductOperationRequest request) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BadRequestException("User is not authenticated");
        }
        User user = (User) auth.getPrincipal();
        if (user == null) {
            throw new BadRequestException("User not found in the authentication context");
        }
        if (!userService.isAdministrator(user)) {
            throw new BadRequestException("User is not an admin");
        }

        if(request.product_type.equals("Car")){
            Car product = new Car();
            product.setId(request.id);

            productService.removeProduct(product);

            return new ProductOperationResponse("success -> deleted product id: " + product.getId());

        } else if (request.product_type.equals("Tire")) {
            Tire product = new Tire();
            product.setId(request.id);

            productService.removeProduct(product);

            return new ProductOperationResponse("success -> deleted product id: " + product.getId());
        } else {
            throw new BadRequestException("Invalid product type");
        }
    }

}
