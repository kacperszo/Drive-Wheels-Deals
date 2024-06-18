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
import pl.drivewheelsdeals.app.request.SetDiscountRequest;
import pl.drivewheelsdeals.app.response.*;
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

    public OrderController(OrderService orderService, ProductService productService, UserService userService) {
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
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
            product.setQuantityInStock(product.getQuantityInStock() - 1);
            productService.updateProduct(product);
            order.getItems().add(oi);
        }

        orderService.update(order);

        customer.setBasket(new ArrayList<>());
        userService.updateCustomer(customer);

        return new CreateOrderResponse("success");
    }

    @PostMapping("/order/set-discount/{id}")
    public SetOrderDiscountResponse setOrderDiscount(@PathVariable Long id, @RequestBody SetDiscountRequest request) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!userService.isAdministrator(user)) {
            throw new BadRequestException("Only admin has access to this resource");
        }

        Order order = orderService.findOrderById(id);

        if(order == null){
            throw new BadRequestException("An order with this id does not exist");
        }

        order.setTotalDiscount(request.discount);
        Order updated = orderService.update(order);

        return new SetOrderDiscountResponse(updated);
    }

    @GetMapping("/ordered-items")
    public Iterable<OrderItem> listOrderedItems() throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!userService.isAdministrator(user)) {
            throw new BadRequestException("Only admin has access to this resource");
        }

        return orderService.findAllOrderedItems();
    }

    @PostMapping("/ordered-items/set-discount/{id}")
    public SetOrderItemDiscountResponse setOrderItemDiscount(@PathVariable Long id, @RequestBody SetDiscountRequest request) throws BadRequestException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        if (!userService.isAdministrator(user)) {
            throw new BadRequestException("Only admin has access to this resource");
        }

        OrderItem orderItem = orderService.findOrderItemById(id);

        if(orderItem == null){
            throw new BadRequestException("There is no item order with this id");
        }

        orderItem.setDiscount(request.discount);
        OrderItem updated = orderService.updateItem(orderItem);

        return new SetOrderItemDiscountResponse(updated);
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
