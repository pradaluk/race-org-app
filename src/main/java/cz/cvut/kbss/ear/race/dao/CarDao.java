package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.model.Car;
import cz.cvut.kbss.ear.race.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class CarDao extends BaseDao<Car> {

    public CarDao() {
        super(Car.class);
    }

    public List<Car> findByUser(User user){
        Objects.requireNonNull(user);
        return em.createNamedQuery("Car.getByUser", Car.class).setParameter("user", user)
                .getResultList();
    }
}
