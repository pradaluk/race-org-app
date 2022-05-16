package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.RacingOrgApp;
import cz.cvut.kbss.ear.race.model.Circuit;
import cz.cvut.kbss.ear.race.model.Race;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import cz.cvut.kbss.ear.race.environment.Generator;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
// Spring runner for JUnit
@RunWith(SpringJUnit4ClassRunner.class)
// DataJpaTest does not load all the application beans, it starts only persistence-related stuff
@DataJpaTest
// Exclude SystemInitializer from the startup, we don't want the admin account here
@ComponentScan(basePackageClasses = RacingOrgApp.class)
public class RaceDaoTest extends BaseDaoTestRunner{
    @PersistenceContext
    private EntityManager em;

    @Autowired
    DataSource dataSource;

    @Autowired
    private RaceDao sut;
    @Autowired
    private CircuitDao cdao;

    @Test
    public void findAllByCircuitReturnRacesOnSpecificCircuit() {
        final Circuit monza = new Circuit();
        monza.setName("Autodromo Nazionale Monza");
        monza.setLocation("Italy");
        cdao.persist(monza);
        final List<Race> races = generateRaces(monza);
        final List<Race> result = sut.findByCircuit(monza);
        /**for(Race race : result){
            System.out.println(race.getName() + " at " + race.getCircuit().getName());
        }**/
        assertEquals(races.size(), result.size());
        races.sort(Comparator.comparing(Race::getName));
        result.sort(Comparator.comparing(Race::getName));
        for (int i = 0; i < races.size(); i++) {
            assertEquals(races.get(i).getId(), result.get(i).getId());
        }
    }

    private List<Race> generateRaces(Circuit circuit) {
        final List<Race> hasCircuit = new ArrayList<>();
        final Circuit other = new Circuit();
        cdao.persist(other);
        other.setName("Circuit de Spa-Francorchamps");
        other.setLocation("Belgium");
        for (int i = 0; i < 10; i++) {
            final Race race = Generator.generateRace();
            race.setCircuit(other);
            if (Generator.randomBoolean()) {
                race.setCircuit(circuit);
                hasCircuit.add(race);
            }
            em.persist(race);
        }
        return hasCircuit;
    }
}
