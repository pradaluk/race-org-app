package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class RaceDao extends BaseDao<Race> {

    public RaceDao() {
        super(Race.class);
    }

    public List<Race> findByCircuit(Circuit circuit){
        Objects.requireNonNull(circuit);
        return em.createNamedQuery("Race.findByCircuit", Race.class).setParameter("circuit", circuit)
                .getResultList();
    }

    public List<Race> findByDriver(User user){
        Objects.requireNonNull(user);
        return em.createNamedQuery("Race.getByDriver", Race.class).setParameter("user", user)
                .getResultList();
    }

    public List<User> getDrivers(Race race){
        Objects.requireNonNull(race);
        return em.createNamedQuery("Race.getDrivers", User.class).setParameter("race", race)
                .getResultList();
    }

    public List<Result> getResults(Race race){
        Objects.requireNonNull(race);
        return race.getResults();
    }

    public void addResult(Race race, User user, Car car){
        Objects.requireNonNull(race);
        Result res = new Result(user,race, car);
        race.addResult(res);
    }

}
