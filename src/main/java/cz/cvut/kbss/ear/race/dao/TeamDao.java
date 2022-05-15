package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.model.Circuit;
import cz.cvut.kbss.ear.race.model.Race;
import cz.cvut.kbss.ear.race.model.Team;
import cz.cvut.kbss.ear.race.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;

@Repository
public class TeamDao extends BaseDao<Team> {

    public TeamDao() {
        super(Team.class);
    }

    public Team findMyTeam(User user){
        Objects.requireNonNull(user);
        try{
            return em.createNamedQuery("Team.findMyTeam", Team.class).setParameter("user", user)
                    .getSingleResult();
        }
        catch (NoResultException e){
            return null;
        }
    }
}
