package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;


@Repository
public class ResultDao extends BaseDao<Result> {

    public ResultDao() {
        super(Result.class);
    }

    public Result findByDriverAndRace(User driver, Race race){
        Objects.requireNonNull(driver);
        Objects.requireNonNull(race);
        return em.createNamedQuery("Result.findByDriverAndRace", Result.class).setParameter("driver", driver).setParameter("race", race)
                .getSingleResult();
    }
    public List<Result> findByDriver(User driver){
        Objects.requireNonNull(driver);
        return em.createNamedQuery("Result.findByDriver", Result.class).setParameter("driver", driver)
                .getResultList();
    }

}