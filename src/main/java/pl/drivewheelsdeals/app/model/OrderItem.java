package pl.drivewheelsdeals.app.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
}
