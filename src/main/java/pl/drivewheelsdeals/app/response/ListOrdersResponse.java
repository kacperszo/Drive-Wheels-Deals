package pl.drivewheelsdeals.app.response;

import pl.drivewheelsdeals.app.model.Order;

import java.util.List;

public class ListOrdersResponse {

    public List<Order> orders;

    public ListOrdersResponse(List<Order> orders){
        this.orders = orders;
    };
}
