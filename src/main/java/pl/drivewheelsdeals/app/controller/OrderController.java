package pl.drivewheelsdeals.app.controller;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.drivewheelsdeals.app.model.*;
import pl.drivewheelsdeals.app.repository.OrderItemRepository;
import pl.drivewheelsdeals.app.response.CreateOrderResponse;
import pl.drivewheelsdeals.app.response.ListOrdersResponse;
import pl.drivewheelsdeals.app.service.OrderService;
import pl.drivewheelsdeals.app.service.ProductService;
import pl.drivewheelsdeals.app.service.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final OrderItemRepository orderItemRepository;

    public OrderController(OrderService orderService, ProductService productService, UserService userService, OrderItemRepository orderItemRepository) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.orderItemRepository = orderItemRepository;
    }

    @GetMapping("/order")
    public ListOrdersResponse listOrdersFromUserById() throws BadRequestException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();


        return new ListOrdersResponse(orderService.findOrdersFromUserById(user.getId()));

    }

    @GetMapping("/order/admin")
    public Iterable<Order> listOrders() throws BadRequestException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!userService.isAdministrator(user)) {
            throw new BadRequestException("Only admin has access to this resource");
        }

        return orderService.findAll();
    }

    @PostMapping("/order/make")
    @Transactional
    public CreateOrderResponse createOrder() throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) auth.getPrincipal();

        if (userService.isAdministrator(user)) {
            throw new BadRequestException("As Admin you cannot make an order");
        }

        Customer customer = (Customer) user;
        List<Product> basket = customer.getBasket();


        Map<Long, Long> productCountMap = basket.stream()
                .collect(Collectors.groupingBy(Product::getId, Collectors.counting()));

        List<Long> productIds = new ArrayList<>(productCountMap.keySet());
        List<Product> productsInStock = productService.getByIds(productIds);

        Map<Long, Product> productMap = productsInStock.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        for (Map.Entry<Long, Long> entry : productCountMap.entrySet()) {
            Long productId = entry.getKey();
            Long countInBasket = entry.getValue();
            Product product = productMap.get(productId);

            if (product == null || product.getQuantityInStock() < countInBasket) {
                throw new BadRequestException("Product with id " + productId + " is not in stock.");
            }
        }

        basket = customer.getBasket();

        Order order = new Order();
        order.setCustomer(customer);

        order = orderService.create(order);

        for (Product product : basket) {
            var oi = new OrderItem(product, order, product.getPrice(), BigDecimal.ZERO);
            order.getItems().add(oi);
        }

        orderService.update(order);

        customer.setBasket(new ArrayList<>());
        userService.updateCustomer(customer);

        return new CreateOrderResponse("success");
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
