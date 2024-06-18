package pl.drivewheelsdeals.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    private Customer customer;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    private Timestamp orderDate;
    private BigDecimal totalDiscount;

    public Order() {
        items = new ArrayList<>();
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Order(Customer customer, List<OrderItem> items, Timestamp orderDate, BigDecimal totalDiscount) {
        this.customer = customer;
        this.items = items;
        this.orderDate = orderDate;
        this.totalDiscount = totalDiscount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public BigDecimal getDiscountedAmount(){
        // Take the price of each product, subtract the discounted amount each time we iterate over an item
        // and in the end, subtract from the sum using totalDiscount
        AtomicReference<BigDecimal> discountedAmount = new AtomicReference<>(BigDecimal.ZERO);
        items.forEach(item -> {
            discountedAmount.set((discountedAmount.get()).add(item.getUnitPrice()).subtract(item.getUnitPrice().multiply(item.getDiscount())));
        });

        discountedAmount.set((discountedAmount.get()).subtract(discountedAmount.get().multiply(totalDiscount)));

        return discountedAmount.get();
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customer, order.customer) && Objects.equals(items, order.items) && Objects.equals(orderDate, order.orderDate) && Objects.equals(totalDiscount, order.totalDiscount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, items, orderDate, totalDiscount);
    }
}
