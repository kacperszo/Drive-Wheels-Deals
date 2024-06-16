package pl.drivewheelsdeals.app.request;

import pl.drivewheelsdeals.app.model.Customer;

public class ListOrdersRequest {

    public long id = -1;

    public Customer customer;

    public ListOrdersRequest(){};

    public void setId(long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
