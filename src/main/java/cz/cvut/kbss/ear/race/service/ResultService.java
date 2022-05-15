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
        dao.update(result);
    }

    @Transactional
    public Result findByDriverAndRace(User user, Race race) {
        return dao.findByDriverAndRace(user, race);
    }

    public List<Result> findByDriver(User user) {
        return dao.findByDriver(user);
    }
}
