package cz.cvut.kbss.ear.race.rest.util;

import cz.cvut.kbss.ear.race.exception.NotFoundException;
import cz.cvut.kbss.ear.race.exception.ValidationException;
import cz.cvut.kbss.ear.race.model.*;
import cz.cvut.kbss.ear.race.security.SecurityUtils;
import cz.cvut.kbss.ear.race.security.model.UserDetails;
import cz.cvut.kbss.ear.race.service.CarService;
import cz.cvut.kbss.ear.race.service.JoinRequestService;
import cz.cvut.kbss.ear.race.service.TeamService;
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

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/teams")
public class TeamController {

    private static final Logger LOG = LoggerFactory.getLogger(TeamController.class);

    private final TeamService teamService;
    private final UserService userService;
    private final JoinRequestService joinRequestService;
    private final UserController userController;
    private final SecurityUtils securityUtils;


    @Autowired
    public TeamController(TeamService teamService, UserService userService, JoinRequestService joinRequestService, UserController userController, SecurityUtils securityUtils) {
        this.teamService = teamService;
        this.userService = userService;
        this.joinRequestService = joinRequestService;
        this.userController = userController;
        this.securityUtils = securityUtils;
    }

    /**
     * Returns all teams.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Team> getTeams() {
        return teamService.findAll();
    }

    /**
     * Updates team.
     * Current user must be the teams driver.
     *
     * @param team new Team data
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateTeam(Principal principal,@PathVariable Integer id, @RequestBody Team team) {
        final User user = userService.find(userController.getCurrent(principal).getId());
        final Team original = teamService.find(id);
        if(original.getTeamOwner().getUsername().equals(user.getUsername()) || user.isAdmin()){
            if (!original.getId().equals(team.getId())) {
                throw new ValidationException("Race identifier in the data does not match the one in the request URL.");
            }
            teamService.update(team);
            LOG.debug("Updated Team {}.", team);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        else{
            LOG.error("User {} is not part of the team {}",user, team);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Removes team.
     * Current user must be the teams driver.
     *
     * @param id team_id to remove
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> removeTeam(Principal principal,@PathVariable Integer id) {
        final User user = userService.find(userController.getCurrent(principal).getId());
        final Team team = teamService.find(id);
        if( user.isAdmin() || team.getTeamOwner().getUsername().equals(user.getUsername()) ){
            final Team original = getTeamById(id);
            if (!original.getId().equals(team.getId())) {
                throw new ValidationException("Team identifier in the data does not match the one in the request URL.");
            }
            for (TeamMembership membership: team.getMemberships()
                 ) {
                User member = userService.find(membership.getUser().getId());
                member.setMembership(null);
                userService.update(member);
            }
            teamService.remove(team);
            LOG.debug("Removed Team {}.", team);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.OK);
        }
        else{
            LOG.error("User {} is not part of the team {}",user, team);
            final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("");
            return new ResponseEntity<>(headers, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Creates team with current user as driver.
     *
     * @param team Team data
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTeam(@RequestBody Team team) {
        final User user = userService.find(securityUtils.getCurrentUser().getId());
        teamService.create(team, user);
        LOG.debug("Created new team {} with owner {}.", team, user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{id}", team.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Returns team by id.
     *
     * @param id Teams id
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team getTeamById(@PathVariable Integer id) {
        final Team t = teamService.find(id);
        if (t == null) {
            throw NotFoundException.create("Team", id);
        }
        return t;
    }

    @GetMapping(value = "/myTeam", produces = MediaType.APPLICATION_JSON_VALUE)
    public Team getTeamMyTeam() {
        final User user = userService.find(securityUtils.getCurrentUser().getId());
        final Team t = teamService.findMyTeam(user);
        return t;
    }
}
