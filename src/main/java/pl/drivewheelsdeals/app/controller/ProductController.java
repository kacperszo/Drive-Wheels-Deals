package pl.drivewheelsdeals.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.drivewheelsdeals.app.model.Product;
import pl.drivewheelsdeals.app.service.ProductService;

@RestController
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
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




}
