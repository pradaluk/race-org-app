package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.RacingOrgApp;
import cz.cvut.kbss.ear.race.config.PersistenceConfig;
import cz.cvut.kbss.ear.race.config.ServiceConfig;
import cz.cvut.kbss.ear.race.model.Car;
import cz.cvut.kbss.ear.race.model.CarClass;
import cz.cvut.kbss.ear.race.model.Circuit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, PersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional(transactionManager = "transactionManager")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackageClasses = RacingOrgApp.class)
public class CircuitServiceTest {

    @Autowired
    private CircuitService circuitService;

    Circuit monza = new Circuit();

    @Before
    public void saveCar(){
        monza.setId(1);
        monza.setLocation("Italy");
        monza.setName("Monza");
        circuitService.persist(monza);
    }

    @Test
    public void NewCircuit_FoundInDatabase() {
        Circuit findCircuit = circuitService.find(1);
        assertEquals(monza,findCircuit);
    }
    @Test
    public void RemoveCircuit_NotFoundInDatabase() {
        Circuit findCircuit = circuitService.find(1);
        assertEquals(monza,findCircuit);

        circuitService.remove(monza);
        findCircuit = circuitService.find(1);
        assertEquals(findCircuit,null);
    }

    @Test
    public void UpdateCircuit_ValuesChanged() {
        Circuit findCircuit = circuitService.find(1);
        assertEquals(monza,findCircuit);

        monza.setName("Autodromo Nazionale Monza");
        circuitService.update(monza);
        findCircuit = circuitService.find(1);
        assertEquals("Autodromo Nazionale Monza",findCircuit.getName());
    }

    @Test
    public void findByCountry_returnsCircuitInItaly() {
        List<Circuit> monzaList = new ArrayList<>();
        monzaList.add(monza);
        List<Circuit> findCircuit = circuitService.findByCountry("Italy");
        assertEquals(monzaList,findCircuit);
    }

    @Test
    public void findByCountry_returnsNoCircuits() {
        List<Circuit> empty = new ArrayList<>();
        List<Circuit> findCircuit = circuitService.findByCountry("Germany");
        assertEquals(empty,findCircuit);
    }

    @Test
    public void findByName_returnsCircuitByName() {
        Circuit findCircuit = circuitService.findByName("Monza");
        assertEquals(monza,findCircuit);
    }

    @Test
    public void findByName_returnsNoCircuitByName() {
        assertThrows(EmptyResultDataAccessException.class,()->{circuitService.findByName("Spa");});
    }
}
