package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.model.Circuit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class CircuitDao extends BaseDao<Circuit> {

    public CircuitDao() {
        super(Circuit.class);
    }

    public List<Circuit> findByCountry(String location){
        Objects.requireNonNull(location);
        return em.createNamedQuery("Circuit.findByCountry", Circuit.class).setParameter("location", location)
                .getResultList();
    }

    public Circuit findByName(String name){
        Objects.requireNonNull(name);
        return em.createNamedQuery("Circuit.findByName", Circuit.class).setParameter("name", name)
                .getSingleResult();
    }
}
