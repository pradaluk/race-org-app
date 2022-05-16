package cz.cvut.kbss.ear.race.rest.util;

import cz.cvut.kbss.ear.race.exception.NotFoundException;
import cz.cvut.kbss.ear.race.exception.ValidationException;
import cz.cvut.kbss.ear.race.model.Circuit;
import cz.cvut.kbss.ear.race.service.CircuitService;
import org.postgresql.util.PSQLException;
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
@RequestMapping("/rest/circuits")
public class CircuitController {
    private static final Logger LOG = LoggerFactory.getLogger(CircuitController.class);

    private final CircuitService circuitService;

    @Autowired
    public CircuitController(CircuitService circuitService) {
        this.circuitService = circuitService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Circuit> getCircuits() {
        return circuitService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addCircuit(@RequestBody Circuit circuit) {
        circuitService.persist(circuit);
        LOG.debug("Added new circuit {}.", circuit);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", circuit.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Circuit getCircuit(@PathVariable Integer id) {
        final Circuit c = circuitService.find(id);
        if (c == null) {
            throw NotFoundException.create("Circuit", id);
        }
        return c;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @PutMapping(value = "/remove/{id}")
    public ResponseEntity<Void> removeCircuit(@PathVariable Integer id) throws PSQLException {
        final Circuit circuit = circuitService.find(id);
        if(circuit!=null){
            try{
                circuitService.remove(circuit);
                LOG.debug("Removed Team {}.", circuit);
                final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
                return new ResponseEntity<>(headers, HttpStatus.OK);
            }
            catch (Exception e)
            {
                LOG.error("Citrcuit is used in a race");
                final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
                return new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else{
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping(value = "/location/{loc}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Circuit> getCircuitByCountry(@PathVariable String loc) {
        final List<Circuit> c = circuitService.findByCountry(loc);
        return c;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCircuit(@PathVariable Integer id, @RequestBody Circuit circuit) {
        final Circuit original = getCircuit(id);
        if (!original.getId().equals(circuit.getId())) {
            throw new ValidationException("Circuit identifier in the data does not match the one in the request URL.");
        }
        circuitService.update(circuit);
        LOG.debug("Updated circuit {}.", circuit);
    }
}
