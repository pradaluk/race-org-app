package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.TeamDao;
import cz.cvut.kbss.ear.race.dao.UserDao;
import cz.cvut.kbss.ear.race.exception.ValidationException;
import cz.cvut.kbss.ear.race.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

@Service
public class TeamService {

    private final TeamDao dao;
    private final UserDao userDao;

    @Autowired
    public TeamService(TeamDao dao, UserDao userDao) {
        this.dao = dao;
        this.userDao = userDao;
    }

    @Transactional
    public void create(Team team, User owner) {
        if(owner.getMembership() != null){
            if(owner.getMembership().getStatus() == TeamMembershipState.ACTIVE){
                throw new ValidationException("Already a member of a team!");
            }
        }
        User user = userDao.find(owner.getId());
        TeamMembership membership = new TeamMembership(user, team, TeamRole.CREATOR);
        team.addMembership(membership);
        owner.setMembership(membership);
        team.setTeamOwner(user);

        dao.persist(team);
        userDao.update(user);
    }
    @Transactional(readOnly = true)
    public List<Team> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Team findMyTeam(User user) {
        return dao.findMyTeam(user);
    }

    @Transactional(readOnly = true)
    public Team find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Team team) {
        dao.persist(team);
    }

    @Transactional(readOnly = true)
    public void remove(Team team) {
        dao.remove(team);
    }

    @Transactional
    public void update(Team team) {
        dao.update(team);
    }

}
