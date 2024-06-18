package pl.drivewheelsdeals.app.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import pl.drivewheelsdeals.app.repository.OrderRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportService {
    private final OrderRepository orderRepository;

    public ReportService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public BigDecimal incomeInTimePeriod(Timestamp from, Timestamp to) {
        if (from == null || to == null) {
            throw new NullPointerException("Dates cannot be null");
        }
        if (from.after(to)) {
            throw new DateTimeException("Start date cannot be after end date");
        }
        AtomicReference<BigDecimal> income = new AtomicReference<>(BigDecimal.ZERO);
        orderRepository.findAll().forEach(order -> {
            if (order.getOrderDate().after(from) && order.getOrderDate().before(to)) {
                var orderIncome = new AtomicReference<>(BigDecimal.ZERO);
                order.getItems().forEach(item -> {
                   orderIncome.set(orderIncome.get().add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
                });
                income.set(income.get().add((orderIncome.get().subtract(orderIncome.get().multiply(order.getTotalDiscount())))));
            }
        });
        return income.get();
    }
}
