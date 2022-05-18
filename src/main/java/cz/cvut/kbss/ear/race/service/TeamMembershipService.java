package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.MembershipDao;
import cz.cvut.kbss.ear.race.dao.TeamDao;
import cz.cvut.kbss.ear.race.dao.UserDao;
import cz.cvut.kbss.ear.race.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class TeamMembershipService {

    private final MembershipDao membershipDao;
    private final UserDao userDao;
    private final TeamDao teamDao;

    @Autowired
    public TeamMembershipService(MembershipDao membershipDao, UserDao userDao, TeamDao teamDao) {
        this.membershipDao = membershipDao;
        this.userDao = userDao;
        this.teamDao = teamDao;
    }

    @Transactional(readOnly = true)
    public List<TeamMembership> findAll() {
        return membershipDao.findAll();
    }

    @Transactional(readOnly = true)
    public TeamMembership find(MembershipId id) {
        return membershipDao.find(id);
    }

    public void createMembership(Team team, User user, TeamRole role) {
        Objects.requireNonNull(team);
        Objects.requireNonNull(user);

        MembershipId membershipId = new MembershipId(team.getId(), user.getId());
        TeamMembership membership = find(membershipId);

        if (membership == null) {
            TeamMembership newMembership = new TeamMembership(user, team, role);
            team.addMembership(newMembership);
            user.setMembership(newMembership);

            membershipDao.persist(newMembership);
        } else {
            membership.setStatus(TeamMembershipState.ACTIVE);
            membershipDao.update(membership);
        }
    }

    @Transactional
    public void removeUserFromProject(User user, Team team) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(team);

        MembershipId membershipId = new MembershipId(team.getId(), user.getId());
        TeamMembership membership = find(membershipId);
        // should check membership if exists

        membership.setStatus(TeamMembershipState.EXPIRED);

        for (JoinRequest j : team.getJoinRequests()) {
            if (j.getUser().getId().equals(user.getId())) {
                team.getJoinRequests().remove(j);
                break;
            }
        }

        membershipDao.update(membership);
        userDao.update(user);
        teamDao.update(team);
    }
}
