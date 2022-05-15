package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.model.Car;
import cz.cvut.kbss.ear.race.model.Circuit;
import cz.cvut.kbss.ear.race.model.JoinRequest;
import cz.cvut.kbss.ear.race.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class JoinRequestDao extends BaseDao<JoinRequest> {
    public JoinRequestDao() {
        super(JoinRequest.class);
    }

    public List<JoinRequest> findJoinRequestByUser(User user) {
        Objects.requireNonNull(user);
        return em.createNamedQuery("JoinRequest.findByUser", JoinRequest.class).setParameter("user", user)
                .getResultList();
    }
}
