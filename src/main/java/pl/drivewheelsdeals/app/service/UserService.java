package pl.drivewheelsdeals.app.service;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import pl.drivewheelsdeals.app.model.Administrator;
import pl.drivewheelsdeals.app.model.Customer;
import pl.drivewheelsdeals.app.model.User;
import pl.drivewheelsdeals.app.repository.AdministratorRepository;
import pl.drivewheelsdeals.app.repository.CustomerRepository;
import pl.drivewheelsdeals.app.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final CustomerRepository customerRepository;

    public UserService(UserRepository userRepository, AdministratorRepository administratorRepository, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.customerRepository = customerRepository;
    }

    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Customer createCustomer(Customer customer) throws BadRequestException {
        if (userRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new BadRequestException("User with provided email already exist.");
        }
        return customerRepository.save((customer));
    }

    public Administrator createAdministrator(Administrator administrator) throws BadRequestException {
        if (userRepository.findByEmail(administrator.getEmail()).isPresent()) {
            throw new BadRequestException("User with provided email already exist.");
        }
        return administratorRepository.save(administrator);
    }

    public boolean isAdministrator(User user) {
        return user instanceof Administrator;
    }


}
