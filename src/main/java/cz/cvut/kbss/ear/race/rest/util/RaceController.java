package cz.cvut.kbss.ear.race.rest.util;

import cz.cvut.kbss.ear.race.exception.NotFoundException;
import cz.cvut.kbss.ear.race.exception.ValidationException;
import cz.cvut.kbss.ear.race.model.Circuit;
import cz.cvut.kbss.ear.race.model.Race;
import cz.cvut.kbss.ear.race.model.User;
import cz.cvut.kbss.ear.race.security.SecurityUtils;
import cz.cvut.kbss.ear.race.service.CircuitService;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/rest/races")
public class RaceController {

    private static final Logger LOG = LoggerFactory.getLogger(RaceController.class);

    private final RaceService raceService;

    private final UserService userService;

    private final CircuitService circuitService;

    private final SecurityUtils securityUtils;

    @Autowired
    public RaceController(RaceService raceService, UserService userService, CircuitService circuitService, SecurityUtils securityUtils) {
        this.raceService = raceService;
        this.userService = userService;
        this.circuitService = circuitService;
        this.securityUtils = securityUtils;
    }

    /**
     * Returns all races.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public List<Race> getRaces() {
        return raceService.findAll();
    }

    /**
     * Updates race
     *
     * @param id Race_id to update
     * @param race new Race data
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRace(@PathVariable Integer id, @RequestBody Race race) {
        final Race original = getRaceById(id);
        if (!original.getId().equals(race.getId())) {
            throw new ValidationException("Race identifier in the data does not match the one in the request URL.");
        }
        raceService.update(race);
        LOG.debug("Updated race {}.", race);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRace(@PathVariable Integer id) {
        final Race original = getRaceById(id);
        raceService.remove(original);
        LOG.debug("Removed race {}.", original);
    }

//    /**
//     * Adds driver to Race.
//     *
//     * @param id Race id
//     * @param user_id User id
//     */
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
//    @PutMapping(value = "/addDriver/{id} {user_id}")
//    public void addDriver(@PathVariable Integer id, @PathVariable Integer user_id) {
//        Race race = raceService.find(id);
//        User user = userService.find(user_id);
//        if(race.getMaxDrivers() < race.getResults().size()){
//            raceService.addDriver(race,user, car);
//            LOG.debug("Added driver {} to race {}.",user, race);
//        }else{
//            throw new ValidationException("Maximum number of drivers already registerd.");
//        }
//    }
//
//    /**
//     * Removes driver from Race.
//     *
//     * @param id Race id
//     * @param user_id User id
//     */
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
//    @PutMapping(value = "/removeDriver/{id} {user_id}")
//    public void removeDriver(@PathVariable Integer id, @PathVariable Integer user_id) {
//        Race race = raceService.find(id);
//        User user = userService.find(user_id);
//        raceService.removeDriver(race,user);
//        LOG.debug("Removed driver {} to race {}.",user, race);
//    }

    /**
     * Creates new Race.
     *
     * @param race Race to create.
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createRace(@RequestBody Race race) throws ParseException {
        raceService.create(race);
        LOG.debug("Created new race {}.", race);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", race.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Return race by id.
     *
     * @param id Race id
     */

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin
    public Race getRaceById(@PathVariable Integer id) {
        final Race r = raceService.find(id);
        if (r == null) {
            throw NotFoundException.create("Race", id);
        }
        return r;
    }

    /**
     * Returns drivers in race.
     *
     * @param id Race id
     */
    @GetMapping(value = "/drivers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getDriversInRace(@PathVariable Integer id) {
        final Race r = raceService.find(id);
        if (r == null) {
            throw NotFoundException.create("Race", id);
        }
        return raceService.getDrivers(r);
    }

    /**
     * Returns races at circuit.
     *
     * @param name Circuit name
     */
    @GetMapping(value = "/circuit/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Race> getRaceByCircuit(@PathVariable String name) {
        Circuit circuit = circuitService.findByName(name);
        if (circuit == null) {
            throw NotFoundException.create("Circuit", circuit);
        }
        final List<Race> r = raceService.findByCircuit(circuit);
        return r;
    }

    /**
     * Returns current users races.
     */
    @GetMapping(value = "/myRaces", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Race> getRaceByCircuit() {
        final User user = securityUtils.getCurrentUser();
        final List<Race> r = raceService.findByDriver(user);
        return r;
    }
}
