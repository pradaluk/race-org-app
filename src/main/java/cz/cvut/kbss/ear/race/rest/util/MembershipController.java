package cz.cvut.kbss.ear.race.rest.util;

import cz.cvut.kbss.ear.race.model.*;
import cz.cvut.kbss.ear.race.security.SecurityUtils;
import cz.cvut.kbss.ear.race.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/memberships")

public class MembershipController {

    private static final Logger LOG = LoggerFactory.getLogger(MembershipController.class);

    private final TeamService teamService;
    private final JoinRequestService joinRequestService;
    private final TeamMembershipService membershipService;
    private final UserService userService;
    private final SecurityUtils securityUtils;

    @Autowired
    public MembershipController(TeamService teamService, JoinRequestService joinRequestService,
                                TeamMembershipService membershipService, UserService userService, SecurityUtils securityUtils) {
        this.teamService = teamService;
        this.joinRequestService = joinRequestService;
        this.membershipService = membershipService;
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    //@PreAuthorize("isMember(#teamId)")
    @GetMapping(value = "/{teamId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TeamMembership> getMemberships(@PathVariable Integer teamId) {
        return teamService.find(teamId).getMemberships().stream()
                .filter(m -> m.getStatus() == TeamMembershipState.ACTIVE).collect(Collectors.toList());
    }

    @GetMapping(value = "/{teamId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public JoinRequestStatus getJoinRequestStatus(@PathVariable Integer teamId) {
        return joinRequestService.findPendingJoinRequestsByProjectAndUser(teamService.find(teamId),
                securityUtils.getCurrentUser());
    }

    //@PreAuthorize("hasPermission(#teamId, 'CREATOR')")
    @GetMapping(value = "/{teamId}/requests", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<JoinRequest> getJoinRequests(@PathVariable Integer teamId) {
        return teamService.find(teamId).getJoinRequests().stream()
                .filter(j -> j.getStatus() == JoinRequestStatus.PENDING).collect(Collectors.toList());
    }

    //@PreAuthorize("hasPermission(#teamId, 'CREATOR')")
    @PutMapping(value = "/{teamId}/requests")
    public ResponseEntity<Void> resolveJoinRequestToTeam(@PathVariable Integer teamId,
                                                            @RequestBody JoinRequest joinRequest) throws ValidationException {
        Team team = teamService.find(teamId);
        joinRequestService.resolveJoinRequest(team, joinRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@PreAuthorize("isNotMember(#teamId)")
    @PutMapping(value = "/{teamId}/join", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postJoinRequestToTeam(@PathVariable Integer teamId,
                                                         @RequestBody JoinRequest joinRequest) throws ValidationException {
        User user = userService.find(securityUtils.getCurrentUser().getId());
        Team team = teamService.find(teamId);
        joinRequestService.postJoinRequest(joinRequest, team, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PreAuthorize("isNotMember(#teamId)")
//    @PutMapping(value = "/{teamId}/cancel")
//    public ResponseEntity<Void> cancelJoinRequestToTeam(@PathVariable Integer teamId) throws ValidationException {
//        User user = securityUtils.getCurrentUser();
//        Team team = teamService.find(teamId);
//        joinRequestService.cancelJoinRequest(team, user);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    //@PreAuthorize("isMember(#teamId)")
    @DeleteMapping(value = "/{teamId}/leave")
    public ResponseEntity<Void> leaveTeam(@PathVariable Integer teamId) {
        User user = userService.find(securityUtils.getCurrentUser().getId());;
        Team team = teamService.find(teamId);
        membershipService.removeUserFromProject(user, team);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //@PreAuthorize("hasPermission(#teamId, 'CREATOR')")
    @DeleteMapping(value = "/{teamId}/leave/{userId}")
    public ResponseEntity<Void> kickUserFromTeam(@PathVariable Integer teamId, @PathVariable Integer userId) {
        User user = userService.find(userId);
        Team team = teamService.find(teamId);
        membershipService.removeUserFromProject(user, team);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}