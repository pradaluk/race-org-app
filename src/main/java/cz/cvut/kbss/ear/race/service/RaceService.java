package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.RaceDao;
import cz.cvut.kbss.ear.race.dao.ResultDao;
import cz.cvut.kbss.ear.race.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class RaceService {

    private final RaceDao dao;
    private final ResultDao resDao;

    public RaceService(RaceDao dao, ResultDao resDao) {
        this.dao = dao;
        this.resDao = resDao;
    }

    @Transactional(readOnly = true)
    public Race find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Race> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Race> findByCircuit(Circuit circuit){
        return dao.findByCircuit(circuit);
    }

    @Transactional(readOnly = true)
    public List<Race> findByDriver(User user){
        return dao.findByDriver(user);
    }

    @Transactional
    public Race create(Race race) {
        dao.persist(race);
        return race;
    }
    @Transactional
    public void addDriver(Race race, User driver, Car car) {
        dao.addResult(race, driver, car);
        //check if already in ?
        Objects.requireNonNull(race);
        Objects.requireNonNull(driver);
        dao.update(race);
    }

    @Transactional
    public void removeDriver(Race race, User driver) {
        Objects.requireNonNull(race);
        Objects.requireNonNull(driver);
        Result res = resDao.findByDriverAndRace(driver, race);
        race.removeResult(res);
        dao.update(race);
        resDao.remove(res);
    }

    @Transactional
    public List<User> getDrivers(Race race) {
        return dao.getDrivers(race);
    }

    @Transactional
    public void update(Race race) {
        dao.update(race);
    }

    @Transactional
    public void remove(Race race) {
        dao.remove(race);
    }
}
