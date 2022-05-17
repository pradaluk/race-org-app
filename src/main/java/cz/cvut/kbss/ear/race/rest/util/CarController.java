package cz.cvut.kbss.ear.race.rest.util;

import cz.cvut.kbss.ear.race.exception.NotFoundException;
import cz.cvut.kbss.ear.race.exception.ValidationException;
import cz.cvut.kbss.ear.race.model.Car;
import cz.cvut.kbss.ear.race.model.User;
import cz.cvut.kbss.ear.race.security.SecurityUtils;
import cz.cvut.kbss.ear.race.security.model.AuthenticationToken;
import cz.cvut.kbss.ear.race.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.xml.bind.PrintConversionEvent;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/cars")
public class CarController {

    private static final Logger LOG = LoggerFactory.getLogger(CarController.class);

    private final CarService carService;
    private final SecurityUtils securityUtils;

    @Autowired
    public CarController(CarService carService, SecurityUtils securityUtils) {
        this.carService = carService;
        this.securityUtils = securityUtils;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Car> getCars() {
        return carService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCar(@PathVariable Integer id, @RequestBody Car car) {
        final Car original = getCarById(id);
        if (!original.getId().equals(car.getId())) {
            throw new ValidationException("Car identifier in the data does not match the one in the request URL.");
        }
        carService.update(car);
        LOG.debug("Updated car {}.", original);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCar(@PathVariable Integer id) {
        final Car original = getCarById(id);
        if (original == null) {
            throw new ValidationException("Car not found.");
        }
        carService.remove(original);
        LOG.debug("Removed car {}.", original);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCar(@RequestBody Car car, Principal principal) {
        final AuthenticationToken auth = (AuthenticationToken) principal;
        final User user = auth.getPrincipal().getUser();
        car.setOwner(user);
        carService.persist(car);
        LOG.debug("Created new car {}.", car);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", car.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Car getCarById(@PathVariable Integer id) {
        final Car c = carService.find(id);
        if (c == null) {
            throw NotFoundException.create("Car", id);
        }
        return c;
    }
    @PermitAll
    @GetMapping(value = "/getMyCars", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Car> getMyCars() {
        final User user = securityUtils.getCurrentUser();
        final List<Car> cars = carService.findByUser(user);
        return cars;
    }
}
