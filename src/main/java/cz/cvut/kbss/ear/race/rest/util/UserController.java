package cz.cvut.kbss.ear.race.rest.util;

import cz.cvut.kbss.ear.race.model.Car;
import cz.cvut.kbss.ear.race.model.Race;
import cz.cvut.kbss.ear.race.model.Role;
import cz.cvut.kbss.ear.race.model.User;
import cz.cvut.kbss.ear.race.security.SecurityUtils;
import cz.cvut.kbss.ear.race.security.model.UserDetails;
import cz.cvut.kbss.ear.race.service.CarService;
import cz.cvut.kbss.ear.race.service.RaceService;
import cz.cvut.kbss.ear.race.service.UserService;
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
import java.security.Principal;

@RestController
@RequestMapping("/rest/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RaceService raceService;
    private final SecurityUtils securityUtils;
    private final CarService carService;

    @Autowired
    public UserController(UserService userService, RaceService raceService, SecurityUtils securityUtils, CarService carService) {
        this.userService = userService;
        this.raceService = raceService;
        this.securityUtils = securityUtils;
        this.carService = carService;
    }

    /**
     * Registers a new user.
     *
     * @param user User data
     */
    @PreAuthorize("(!#user.isAdmin() && anonymous) || hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody User user) {
        userService.persist(user);
        LOG.debug("User {} successfully registered.", user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Registers current user for Race.
     *
     * @param id Race id
     */
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    @PostMapping(value = "/registerForRace/{id}/{carId}")
    public void registerForRace(@PathVariable Integer id, @PathVariable Integer carId) {
        User user = this.getCurrent();
        Race race = raceService.find(id);
        Car car = carService.find(carId);
        boolean success = userService.registerForRace(race,user, car);
        if(success){
            LOG.debug("User {} successfully registered for race {}", user, race);
        }
    }

    /**
     * Withdraws current user for Race.
     *
     * @param id Race id
     */
    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    @PostMapping(value = "/withdraw/{id}")
    public void withdrawFromRace(@PathVariable Integer id) {
        final User user = userService.find(getCurrent().getId());
        final Race race = raceService.find(id);
        boolean success = false;
        success = userService.withdrawFromRace(race,user);
        if(success){
            LOG.debug("User {} successfully withdrawn for race {}", user, race);
        }
    }

    /**
     * Return current user.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_GUEST', 'ROLE_ORGANIZER')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getCurrent() {
        final User user = userService.find(securityUtils.getCurrentUser().getId());
        return user;
    }
    @PermitAll
    @GetMapping(value = "/isLoggedIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isLoggedIn(Principal principal) {
        boolean isLoggedIn =(principal!=null) ? true : false;
        return isLoggedIn;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Integer id) {
        User user = userService.find(id);
        return user;
    }

    @PermitAll
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User current = securityUtils.getCurrentUser();
        if(current.getId() != id && current.getRole() != Role.ADMIN){
            LOG.error("Not updating current user.");
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
        }
        User original_user = userService.find(id);
        user.setPassword(original_user.getPassword());
        user.setRole(original_user.getRole());
        if(original_user == null){
            LOG.error("User not found.");
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
        else{
            userService.update(user);
            LOG.info("Updated user info for {}.",original_user);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PutMapping(value = "/remove/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Integer id) {
        User user = userService.find(id);
        if(user == null){
            LOG.error("User not found.");
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
        else{
            userService.remove(user);
            LOG.info("Removed user {}.",user);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
    }
}
