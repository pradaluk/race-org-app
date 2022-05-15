package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.ChampionshipDao;
import cz.cvut.kbss.ear.race.model.Championship;
import cz.cvut.kbss.ear.race.model.Race;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
public class ChampionshipService {

    private final ChampionshipDao dao;

    @Autowired
    public ChampionshipService(ChampionshipDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public Championship find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public void addRace(Championship championship, Race race) {
        championship.addRace(race);
        dao.update(championship);
    }

    @Transactional(readOnly = true)
    public void removeRace(Championship championship, Race race) {
        championship.removeRace(race);
        dao.update(championship);
    }

    @Transactional(readOnly = true)
    public List<Championship> findAll() {
        return dao.findAll();
    }

    @Transactional
    public void persist(Championship championship) {
        Objects.requireNonNull(championship);
        dao.persist(championship);
    }
    @Transactional
    public void update(Championship championship) {
        dao.update(championship);
    }

    @Transactional
    public void remove(Championship championship) {
        dao.remove(championship);
    }
}
