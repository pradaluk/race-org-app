package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.CarDao;
import cz.cvut.kbss.ear.race.model.Car;
import cz.cvut.kbss.ear.race.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarService {

    private final CarDao dao;

    @Autowired
    public CarService(CarDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Car> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Car find(Integer id) {
        return dao.find(id);
    }

    @Transactional
    public void persist(Car car) {
        dao.persist(car);
    }

    @Transactional
    public void update(Car car) {
        dao.update(car);
    }

    @Transactional
    public void remove(Car car) {
        dao.remove(car);
    }

    public List<Car> findByUser(User user) {
        return dao.findByUser(user);
    }
}
