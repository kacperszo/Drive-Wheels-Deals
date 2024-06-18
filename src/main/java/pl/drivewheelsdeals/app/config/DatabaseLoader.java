package pl.drivewheelsdeals.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.drivewheelsdeals.app.model.Administrator;
import pl.drivewheelsdeals.app.model.Car;
import pl.drivewheelsdeals.app.model.Tire;
import pl.drivewheelsdeals.app.repository.*;

import java.math.BigDecimal;

@Configuration
public class DatabaseLoader {
    @Bean
    CommandLineRunner populateDatabase(CarRepository carRepository,
                                       TireRepository tireRepository,
                                       AdministratorRepository administratorRepository,
                                       CustomerRepository customerRepository,
                                       OrderRepository orderRepository,
                                       PasswordEncoder passwordEncoder
    ) {
        return args -> {
            System.out.println("Populating database...");
            //create cars
            carRepository.save(new Car("Ford", "Mustang", 2007, BigDecimal.valueOf(100000),12));
            carRepository.save(new Car("Fiat", "126 ", 1987, BigDecimal.valueOf(12000),5));
            carRepository.save(new Car("BMW", "M3", 2007, BigDecimal.valueOf(25000),2));
            //create tires
            tireRepository.save(new Tire("Michelin", "195/65 R 15 91H", BigDecimal.valueOf(1200),12));
            tireRepository.save(new Tire("Michelin", "150/30 R 12 82H", BigDecimal.valueOf(2200),15));
            //create administrator
            administratorRepository.save(new Administrator("Admin", "Adminowski", "admin@local.local", passwordEncoder.encode("password")));
            System.out.println("Database populated");
        };
    }
}
