package pl.drivewheelsdeals.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

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

    @ManyToOne(fetch = FetchType.EAGER)
    public Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Order order;
    private BigDecimal unitPrice;
    private BigDecimal discount;

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && Objects.equals(product, orderItem.product) && Objects.equals(order, orderItem.order) && Objects.equals(unitPrice, orderItem.unitPrice) && Objects.equals(discount, orderItem.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, order, unitPrice, discount);
    }
}
