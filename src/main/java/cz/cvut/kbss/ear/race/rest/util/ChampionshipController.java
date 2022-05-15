package cz.cvut.kbss.ear.race.rest.util;

import cz.cvut.kbss.ear.race.exception.NotFoundException;
import cz.cvut.kbss.ear.race.exception.ValidationException;
import cz.cvut.kbss.ear.race.model.Championship;
import cz.cvut.kbss.ear.race.model.Race;
import cz.cvut.kbss.ear.race.service.ChampionshipService;
import cz.cvut.kbss.ear.race.service.RaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/championships")
public class ChampionshipController {

    private static final Logger LOG = LoggerFactory.getLogger(ChampionshipController.class);

    private final ChampionshipService championshipService;
    private final RaceService raceService;

    @Autowired
    public ChampionshipController(ChampionshipService championshipService, RaceService raceService) {
        this.championshipService = championshipService;
        this.raceService = raceService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Championship> getChampionships() {
        return championshipService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZOR')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateChampionship(@PathVariable Integer id, @RequestBody Championship championship) {
        final Championship original = getChampionshipById(id);
        if (!original.getId().equals(championship.getId())) {
            throw new ValidationException("Championship identifier in the data does not match the one in the request URL.");
        }
        championshipService.update(championship);
        LOG.debug("Updated championship {}.", championship);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createChampionship(@RequestBody Championship championship) {
        championshipService.persist(championship);
        LOG.debug("Created new championship {}.", championship);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", championship.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Championship getChampionshipById(@PathVariable Integer id) {
        final Championship c = championshipService.find(id);
        if (c == null) {
            throw NotFoundException.create("Championship", id);
        }
        return c;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZOR')")
    @PutMapping(value = "/addRace/{id} {race_id}")
    public void addRace(@PathVariable Integer id, @PathVariable Integer race_id) {
        final Championship championship = getChampionshipById(id);
        final Race race = raceService.find(race_id);
        championshipService.addRace(championship,race);
        LOG.debug("Added race {} to championship {}.", race, championship);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZOR')")
    @PutMapping(value = "/removeRace/{id} {race_id}")
    public void removeRace(@PathVariable Integer id, @PathVariable Integer race_id) {
        final Championship championship = getChampionshipById(id);
        final Race race = raceService.find(race_id);
        championshipService.removeRace(championship,race);
        LOG.debug("Removed race {} to championship {}.", race, championship);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZOR')")
    @PutMapping(value = "/remove/{id}")
    public void removeChampionship(@PathVariable Integer id) {
        final Championship championship = getChampionshipById(id);
        championshipService.remove(championship);
        LOG.debug("Removed championship {}.", championship);
    }
}
