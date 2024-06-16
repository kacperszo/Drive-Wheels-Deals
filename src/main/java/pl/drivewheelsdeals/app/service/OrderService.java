package pl.drivewheelsdeals.app.service;

import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import pl.drivewheelsdeals.app.model.Customer;
import pl.drivewheelsdeals.app.model.Order;
import pl.drivewheelsdeals.app.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    public Order findOrderById(long id) {
        return orderRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    public List<Order> findOrdersFromUserById(long id) {
        Customer c = (Customer) userService.findUserById(id);
        return c.getOrders();
    }
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }
}
