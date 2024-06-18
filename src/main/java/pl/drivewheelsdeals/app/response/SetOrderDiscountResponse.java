package pl.drivewheelsdeals.app.response;

import pl.drivewheelsdeals.app.model.Order;
import pl.drivewheelsdeals.app.model.OrderItem;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class SetOrderDiscountResponse {

    public Long id;
    public List<OrderItem> items;
    public Timestamp orderDate;
    public BigDecimal totalDiscount;
    public BigDecimal savedAmount;
    public Long clientId;

    public SetOrderDiscountResponse(Order order){
        this.id = order.getId();
        this.clientId = order.getCustomer().getId();
        this.items = order.getItems();
        this.orderDate = order.getOrderDate();
        this.totalDiscount = order.getTotalDiscount();
        this.savedAmount = order.getDiscountedAmount();
    }
}
