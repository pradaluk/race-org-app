package cz.cvut.kbss.ear.race.dao;
import cz.cvut.kbss.ear.race.exception.PersistenceException;
import cz.cvut.kbss.ear.race.model.MembershipId;
import cz.cvut.kbss.ear.race.model.TeamMembership;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class MembershipDao extends BaseDao<TeamMembership> {

    public MembershipDao() {
        super(TeamMembership.class);
    }

    public TeamMembership find(MembershipId id) {
        Objects.requireNonNull(id);
        return em.find(type, id);
    }
}
