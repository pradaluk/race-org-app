package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.RaceDao;
import cz.cvut.kbss.ear.race.dao.ResultDao;
import cz.cvut.kbss.ear.race.dao.UserDao;
import cz.cvut.kbss.ear.race.model.*;
import cz.cvut.kbss.ear.race.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(cz.cvut.kbss.ear.race.service.UserService.class);

    private final UserDao dao;
    private final RaceDao raceDao;
    private final ResultDao resDao;

    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return dao.findByUsername(username);
    }

    @Autowired
    public UserService(UserDao dao, RaceDao raceDao, ResultDao resDao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.raceDao = raceDao;
        this.resDao = resDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void persist(User user) {
        Objects.requireNonNull(user);
        user.encodePassword(passwordEncoder);
        if (user.getRole() == null) {
            user.setRole(Constants.DEFAULT_ROLE);
        }
        dao.persist(user);
    }

    @Transactional
    public boolean registerForRace(Race race, User user, Car car) {
        Objects.requireNonNull(race);
        Objects.requireNonNull(user);
        List<User> drivers = raceDao.getDrivers(race);
        for(User u : drivers){
            LOG.debug(u.getUsername());
            if(u.getUsername().equals(user.getUsername())){
                LOG.info("User {} is already registered for the race.", user);
                return false;
            }
        }
        boolean valid = false;
        for (CarClass car_class: race.getClasses()
             ) {
            if(car.getCarClass() == car_class){
                valid = true;
            }
        }
        if(valid){
            race.addResult(new Result(user, race, car));
            raceDao.update(race);
            return true;
        }else {
            LOG.info("Car {} {} doesnt have appropriate class", car.getMaker(), car.getModel());
            return false;
        }
    }

    @Transactional
    public boolean withdrawFromRace(Race race, User user) {
        Objects.requireNonNull(race);
        Objects.requireNonNull(user);
        boolean inRace = false;
        List<User> drivers = raceDao.getDrivers(race);
        for(User u : drivers){
            LOG.debug(u.getUsername() + " is in the Race");
            if(u.getId().equals(user.getId())){
                inRace = true;
                break;
            }
        }
        if(!inRace){
            LOG.info("User {} is not registered for the race.", user);
            return false;
        }
        Result res = resDao.findByDriverAndRace(user, race);
        LOG.info(res.getUser().getUsername() + " " + res.getRace().getName());
        race.removeResult(res);
        raceDao.update(race);
        resDao.remove(res);
        return true;
    }

    @Transactional(readOnly = true)
    public boolean exists(String username) {
        return dao.findByUsername(username) != null;
    }

    @Transactional(readOnly = true)
    public void remove(User user) {
        dao.remove(user);
    }

    @Transactional
    public void update(User user) {
        dao.update(user);
    }
}
