package cz.cvut.kbss.ear.race.rest.util;

import cz.cvut.kbss.ear.race.exception.ValidationException;
import cz.cvut.kbss.ear.race.model.Race;
import cz.cvut.kbss.ear.race.model.Result;
import cz.cvut.kbss.ear.race.model.User;
import cz.cvut.kbss.ear.race.security.SecurityUtils;
import cz.cvut.kbss.ear.race.service.CircuitService;
import cz.cvut.kbss.ear.race.service.RaceService;
import cz.cvut.kbss.ear.race.service.ResultService;
import cz.cvut.kbss.ear.race.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/results")
public class ResultController {

    private static final Logger LOG = LoggerFactory.getLogger(ResultController.class);

    private final ResultService resultService;
    private final RaceService raceService;
    private final SecurityUtils securityUtils;
    @Autowired
    public ResultController(ResultService resultService, SecurityUtils securityUtils, RaceService raceService) {
        this.resultService = resultService;
        this.securityUtils = securityUtils;
        this.raceService = raceService;

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ORGANIZER')")
    @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateResults(@RequestBody Result result) {
        resultService.update(result);
        LOG.debug("Updated race {}.", result);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @GetMapping(value = "/myResults")
    public List<Result> myResults() {
        final User user = securityUtils.getCurrentUser();
        final List<Result> results = resultService.findByDriver(user);
        return results;
    }

}
