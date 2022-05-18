package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.ResultDao;
import cz.cvut.kbss.ear.race.model.Race;
import cz.cvut.kbss.ear.race.model.Result;
import cz.cvut.kbss.ear.race.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResultService {

    private final ResultDao dao;

    public ResultService(ResultDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void update(Result result) {
        Result orig = dao.findByDriverAndRace(result.getUser(),result.getRace());
        orig.setStartPos(result.getStartPos());
        orig.setFinishPos(result.getFinishPos());
        dao.update(orig);
    }

    @Transactional
    public void persist(Result result) {
        dao.persist(result);
    }

    @Transactional
    public Result findByDriverAndRace(User user, Race race) {
        return dao.findByDriverAndRace(user, race);
    }

    public List<Result> findByDriver(User user) {
        return dao.findByDriver(user);
    }
}
