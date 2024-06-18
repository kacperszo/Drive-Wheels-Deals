package pl.drivewheelsdeals.app.service;

import org.springframework.stereotype.Service;
import pl.drivewheelsdeals.app.model.Car;
import pl.drivewheelsdeals.app.model.Tire;
import pl.drivewheelsdeals.app.reports.BrandsSold;
import pl.drivewheelsdeals.app.reports.SoldToCountry;
import pl.drivewheelsdeals.app.reports.TypeSold;
import pl.drivewheelsdeals.app.reports.CustomersPerCountry;
import pl.drivewheelsdeals.app.repository.CustomerRepository;
import pl.drivewheelsdeals.app.repository.OrderRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public ReportService(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    public BigDecimal sumaricIncome() {
        AtomicReference<BigDecimal> income = new AtomicReference<>(BigDecimal.ZERO);
        orderRepository.findAll().forEach(order -> {
            var orderIncome = new AtomicReference<>(BigDecimal.ZERO);
            order.getItems().forEach(item -> {
               orderIncome.set(orderIncome.get().add(item.getUnitPrice().subtract(item.getUnitPrice().multiply(item.getDiscount()))));
            });
            income.set(income.get().add((orderIncome.get().subtract(orderIncome.get().multiply(order.getTotalDiscount() != null ? order.getTotalDiscount() : BigDecimal.valueOf(0))))));
        });
        return income.get();
    }

    public List<TypeSold> soldItemsOfEachType() {
        AtomicInteger cars = new AtomicInteger();
        AtomicInteger tires = new AtomicInteger();

        orderRepository.findAll().forEach(order -> {
           order.getItems().forEach(item -> {
               if (item.getProduct() instanceof Car) {
                   cars.incrementAndGet();
               } else if (item.getProduct() instanceof Tire) {
                   tires.incrementAndGet();
               }
           });
        });

        var carsSold = new TypeSold("Car", cars.get());
        var tiresSold = new TypeSold("Tire", tires.get());

        return List.of(carsSold, tiresSold);
    }

    public SoldToCountry carsSoldToCountry(String country) {
        if (country == null) {
            throw new NullPointerException("Country cannot be null");
        }

        AtomicInteger carsSold = new AtomicInteger();

        orderRepository.findAll().forEach(order -> {
            var customer = order.getCustomer();
            if (customer == null) {
                throw new NullPointerException("Customer cannot be null");
            }
            if (customer.getCountry().equals(country)) {
                order.getItems().forEach(item -> {
                    if (item.getProduct() instanceof Car) {
                        carsSold.incrementAndGet();
                    }
                });
            }
        });
        return new SoldToCountry(country, carsSold.get());
    }

    public SoldToCountry tiresSoldToCountry(String country) {
        if (country == null) {
            throw new NullPointerException("Country cannot be null");
        }

        AtomicInteger carsSold = new AtomicInteger();

        orderRepository.findAll().forEach(order -> {
            var customer = order.getCustomer();
            if (customer == null) {
                throw new NullPointerException("Customer cannot be null");
            }
            if (customer.getCountry().equals(country)) {
                order.getItems().forEach(item -> {
                    if (item.getProduct() instanceof Tire) {
                        carsSold.incrementAndGet();
                    }
                });
            }
        });
        return new SoldToCountry(country, carsSold.get());
    }

    public List<CustomersPerCountry> getCustomersPerCountry() {
        var customersPerCountryMap = new HashMap<String, Integer>();

        customerRepository.findAll().forEach(customer -> {
            customersPerCountryMap.compute(customer.getCountry(), (k, current) -> current == null ? 1 : current + 1);
        });

        List<CustomersPerCountry> customersPerCountry = new LinkedList<>();
        for (String country : customersPerCountryMap.keySet()) {
            customersPerCountry.add(new CustomersPerCountry(country, customersPerCountryMap.get(country)));
        }
        return customersPerCountry;
    }

    public List<BrandsSold> getCarsSoldByBrand() {
        var brandMap = new HashMap<String, Integer>();

        orderRepository.findAll().forEach(order -> {
            order.getItems().forEach(item -> {
                if (item.getProduct() instanceof Car) {
                    var car = (Car) item.getProduct();
                    if (car.getBrand() != null) {
                        brandMap.compute(car.getBrand(), (k, current) -> current == null ? 1 : current + 1);
                    }
                }
            });
        });
        List<BrandsSold> brandsSold = new LinkedList<>();
        for (String brand : brandMap.keySet()) {
            brandsSold.add(new BrandsSold(brand, brandMap.get(brand)));
        }
        return brandsSold;
    }
}
