package pl.drivewheelsdeals.app.response;

import pl.drivewheelsdeals.app.model.OrderItem;

import java.math.BigDecimal;

public class SetOrderItemDiscountResponse {
    public Long orderItemId;
    public Long orderId;
    public BigDecimal price;
    public BigDecimal discount;

    public SetOrderItemDiscountResponse(OrderItem orderItem){
        this.orderItemId = orderItem.getId();
        this.orderId = orderItem.getOrder().getId();
        this.price = orderItem.getUnitPrice();
        this.discount = orderItem.getDiscount();
    }
}
