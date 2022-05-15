package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.dao.CircuitDao;
import cz.cvut.kbss.ear.race.model.Circuit;
import cz.cvut.kbss.ear.race.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CircuitService {

    private final CircuitDao dao;

    @Autowired
    public CircuitService(CircuitDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public List<Circuit> findAll() {
        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Circuit find(Integer id) {
        return dao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Circuit> findByCountry(String loc) {
        return dao.findByCountry(loc);
    }

    @Transactional(readOnly = true)
    public Circuit findByName(String name) {
        return dao.findByName(name);
    }

    @Transactional
    public void persist(Circuit circuit) {
        Objects.requireNonNull(circuit);
        dao.persist(circuit);
    }

    @Transactional
    public void remove(Circuit circuit) {
        Objects.requireNonNull(circuit);
        dao.remove(circuit);
    }
    @Transactional
    public void update(Circuit circuit) {
        dao.update(circuit);
    }
}
