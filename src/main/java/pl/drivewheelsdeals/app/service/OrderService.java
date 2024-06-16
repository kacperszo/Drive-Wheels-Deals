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

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public Order findOrderById(long id){
        return orderRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    public List<Order> findOrdersFromUserById(long id){
        List<Order> usersOrders = new ArrayList<>();
        for(Order order : orderRepository.findAll()){
            if (order.getCustomer().getId() == id)
                usersOrders.add(order);
        }
        return usersOrders;
    }

    public List<Order> findOrdersFromUserByCustomer(Customer customer){
        List<Order> usersOrders = new ArrayList<>();
        for(Order order : orderRepository.findAll()){
            if (order.getCustomer().equals(customer))
                usersOrders.add(order);
        }
        return usersOrders;
    }
}
