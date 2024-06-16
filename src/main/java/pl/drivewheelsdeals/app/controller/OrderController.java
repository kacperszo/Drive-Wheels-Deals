package pl.drivewheelsdeals.app.controller;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import pl.drivewheelsdeals.app.request.ListOrdersRequest;
import pl.drivewheelsdeals.app.response.ListOrdersResponse;
import pl.drivewheelsdeals.app.service.OrderService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public ListOrdersResponse listOrdersFromUserById(@Valid @RequestBody ListOrdersRequest request) throws BadRequestException {
        try {
            return new ListOrdersResponse(orderService.findOrdersFromUserById(request.id));
        } catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }
//
//    @GetMapping
//    public ListOrdersResponse listOrdersFromUserByCustomer(@Valid @RequestBody ListOrdersRequest request) throws BadRequestException {
//        try {
//            return new ListOrdersResponse(orderService.findOrdersFromUserByCustomer(request.customer));
//        } catch (Exception e){
//            throw new BadRequestException(e.getMessage());
//        }
//    }

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
