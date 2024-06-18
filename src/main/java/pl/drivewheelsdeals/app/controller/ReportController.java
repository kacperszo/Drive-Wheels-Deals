package pl.drivewheelsdeals.app.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.drivewheelsdeals.app.model.User;
import pl.drivewheelsdeals.app.reports.CustomersPerCountry;
import pl.drivewheelsdeals.app.reports.SoldToCountry;
import pl.drivewheelsdeals.app.reports.TypeSold;
import pl.drivewheelsdeals.app.service.ReportService;
import pl.drivewheelsdeals.app.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ReportController {
    private final ReportService reportService;
    private final UserService userService;

    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    private boolean authenticate() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) auth.getPrincipal();

        return userService.isAdministrator(user);
    }

    @GetMapping("/report/income")
    public BigDecimal getIncome() throws BadRequestException {
        if (!authenticate()) {
            throw new BadRequestException("User is not an administrator!");
        }
        return reportService.sumaricIncome();
    }

    @GetMapping("/report/type-sold")
    public List<TypeSold> getTypeSold() throws BadRequestException {
        if (!authenticate()) {
            throw new BadRequestException("User is not an administrator!");
        }

        return reportService.soldItemsOfEachType();
    }

    @GetMapping("/report/cars-sold-to-country/{country}")
    public SoldToCountry getSoldToCountry(@PathVariable String country) throws BadRequestException {
        if (!authenticate()) {
            throw new BadRequestException("User is not an administrator!");
        }
        return reportService.carsSoldToCountry(country);
    }

    @GetMapping("/report/tires-sold-to-country/{country}")
    public SoldToCountry getTiresToCountry(@PathVariable String country) throws BadRequestException {
        if (!authenticate()) {
            throw new BadRequestException("User is not an administrator!");
        }
        return reportService.tiresSoldToCountry(country);
    }

    @GetMapping("/report/customers-per-country")
    public List<CustomersPerCountry> getCustomersPerCountry() throws BadRequestException {
        if (!authenticate()) {
            throw new BadRequestException("User is not an administrator!");
        }
        return reportService.getCustomersPerCountry();
    }
}
