package pl.drivewheelsdeals.app.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public OrderItem() {
    }

    public OrderItem(Product product, Order order, BigDecimal unitPrice, BigDecimal discount) {
        this.product = product;
        this.order = order;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    @ManyToOne
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;
    private BigDecimal unitPrice;
    private BigDecimal discount;

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Product getProduct() {
        return product;
    }
}
