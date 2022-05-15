package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.JoinRequestDao;
import cz.cvut.kbss.ear.race.dao.TeamDao;
import cz.cvut.kbss.ear.race.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.ValidationException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class JoinRequestService {

    private final JoinRequestDao joinRequestDao;
    private final TeamDao teamDao;
    private final TeamMembershipService membershipService;

    public JoinRequestService(JoinRequestDao joinRequestDao, TeamDao teamDao,
                              TeamMembershipService membershipService) {
        this.joinRequestDao = joinRequestDao;
        this.teamDao = teamDao;
        this.membershipService = membershipService;
    }

    @Transactional(readOnly = true)
    public JoinRequest findById(Integer id) {
        return joinRequestDao.find(id);
    }

    public JoinRequestStatus findPendingJoinRequestsByProjectAndUser(Team team, User user) {
        for (JoinRequest j : team.getJoinRequests()) {
            if ((j.getUser().getId() == user.getId()) && (j.getStatus() == JoinRequestStatus.PENDING)) {
                return j.getStatus();
            }
        }
        return null;
    }

    public List<JoinRequest> findJoinRequestByUser(User user) {
        return joinRequestDao.findJoinRequestByUser(user);
    }

    @Transactional
    public void persist(JoinRequest joinRequest) {
        Objects.requireNonNull(joinRequest);
        joinRequestDao.persist(joinRequest);
    }

    @Transactional
    public void update(JoinRequest joinRequest) {
        joinRequestDao.update(joinRequest);
    }

    @Transactional
    public void remove(JoinRequest joinRequest) {
        Objects.requireNonNull(joinRequest);
        joinRequestDao.remove(joinRequest);
    }

    @Transactional
    public void postJoinRequest(JoinRequest joinRequest, Team team, User user) throws ValidationException {
        Objects.requireNonNull(joinRequest);
        Objects.requireNonNull(team);
        Objects.requireNonNull(user);
        if(user.getMembership() != null){
            if(user.getMembership().getStatus() == TeamMembershipState.ACTIVE) {
                throw new ValidationException("Already a member of another team!");
            }
        }
        JoinRequest foundJoinRequest = null;
        for (JoinRequest j : team.getJoinRequests()) {
            if (j.getUser().getId() == user.getId()) {
                if (j.getStatus() == JoinRequestStatus.PENDING) {
                    throw new ValidationException("Request already sent!");
                }
                if (j.getStatus() == JoinRequestStatus.ACCEPTED) {
                    throw new ValidationException("Request already accepted!");
                }
                if (j.getStatus() == JoinRequestStatus.REJECTED) {
                    throw new ValidationException("Request already rejected!");
                }
                foundJoinRequest = j;
                break;
            }
        }

        if (foundJoinRequest != null) {
            foundJoinRequest.setMessage(joinRequest.getMessage());
            foundJoinRequest.setStatus(JoinRequestStatus.PENDING);
        } else {
            joinRequest.setUser(user);
            team.addJoinRequest(joinRequest);
        }


        teamDao.update(team);
    }

    @Transactional
    public void resolveJoinRequest(Team team, JoinRequest joinRequest) throws ValidationException {
        Objects.requireNonNull(team);
        Objects.requireNonNull(joinRequest);
        JoinRequest check = joinRequestDao.find(joinRequest.getId());
        if(check.getUser().getMembership() != null){
            if(joinRequest.getUser().getMembership().getStatus() == TeamMembershipState.ACTIVE) {
                throw new ValidationException("Already a member of another team!");
            }
        }
        for (JoinRequest j : team.getJoinRequests()) {
            if (j.getId().equals(joinRequest.getId())) {
                if ((j.getStatus() == JoinRequestStatus.ACCEPTED)) {
                    throw new ValidationException("Request already accepted!");
                }
                if ((j.getStatus() == JoinRequestStatus.REJECTED)) {
                    throw new ValidationException("Request already rejected!");
                }

                j.setDateTimeResolved(new Date());
                if (joinRequest.getStatus() == JoinRequestStatus.ACCEPTED) {
                    j.setStatus(JoinRequestStatus.ACCEPTED);
                    teamDao.update(team);
                    membershipService.createMembership(team, j.getUser(), TeamRole.MEMBER);
                } else {
                    j.setStatus(JoinRequestStatus.REJECTED);
                    teamDao.update(team);
                }
                return;
            }
        }

        throw new ValidationException("Invalid request!");
    }
}
