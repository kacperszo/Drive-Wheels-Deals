package pl.drivewheelsdeals.app.service;

import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.drivewheelsdeals.app.model.Customer;
import pl.drivewheelsdeals.app.model.Order;
import pl.drivewheelsdeals.app.model.OrderItem;
import pl.drivewheelsdeals.app.repository.OrderItemRepository;
import pl.drivewheelsdeals.app.repository.OrderRepository;


import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, UserService userService, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderItemRepository = orderItemRepository;
    }

    public Order findOrderById(long id) {
        return orderRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    public List<Order> findOrdersFromUserById(long id) {
        Customer c = (Customer) userService.findUserById(id);
        return c.getOrders();
    }
    @Transactional
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public Iterable<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order update(Order order) {
        return orderRepository.save(order);
    }

    public OrderItem findOrderItemById(long id){
        return orderItemRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    public Iterable<OrderItem> findAllOrderedItems(){
        return orderItemRepository.findAll();
    }

    public OrderItem updateItem(OrderItem item){
        return orderItemRepository.save(item);
    };
}
