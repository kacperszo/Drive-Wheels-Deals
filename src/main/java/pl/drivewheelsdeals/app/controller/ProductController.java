package pl.drivewheelsdeals.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.drivewheelsdeals.app.model.Product;
import pl.drivewheelsdeals.app.service.ProductService;

@RestController
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/product")
    public Page<Product> listProducts(Integer page, Integer size) {
        if(page == null) page=0;
        if(size == null) size=10;
        PageRequest pageRequest = PageRequest.of(page, size);
        return productService.getAllProducts(pageRequest);
    }


}
