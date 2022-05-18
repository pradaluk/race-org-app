package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.RacingOrgApp;
import cz.cvut.kbss.ear.race.config.PersistenceConfig;
import cz.cvut.kbss.ear.race.config.ServiceConfig;
import cz.cvut.kbss.ear.race.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, PersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional(transactionManager = "transactionManager")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackageClasses = RacingOrgApp.class)
public class RaceServiceTest {

    @Autowired
    private RaceService raceService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private UserService userService;

    @Autowired
    private CircuitService circuitService;

    Race race = new Race();
    Circuit circuit = new Circuit();
    User organizer = new User();
    User driver = new User();
    Result result = new Result();

    @Before
    public void setUp(){
        organizer.setPassword("test");
        organizer.setUsername("test");
        organizer.setEmail("test");
        organizer.setFirstName("test");
        organizer.setLastName("test");
        organizer.setAge(20);
        race.setOrganizor(organizer);
        userService.persist(organizer);

        driver.setPassword("test");
        driver.setUsername("driver");
        driver.setEmail("test");
        driver.setFirstName("test");
        driver.setLastName("test");
        driver.setAge(20);
        userService.persist(driver);

        circuit.setName("Le Mans");
        circuit.setLocation("France");
        race.setCircuit(circuit);
        circuitService.persist(circuit);

        result.setUser(driver);
        result.setRace(race);
        race.addResult(result);
        resultService.persist(result);

        race.setName("24H of Le Mans");
        race.setRace_date(new Date());
        List<CarClass> classes = new ArrayList<>();
        classes.add(CarClass.GT3);
        race.setClasses(classes);
        race.setEor_date(new Date());
        race.setMaxDrivers(20);
        race.setId(1);
        raceService.create(race);
    }

    @Test
    public void newRace_findRaceInDatabase(){
        Race foundRace = raceService.find(1);
        Assert.assertEquals(race,foundRace);
    }

    @Test
    public void findRaceByCircuit_findsRace(){
        List<Race> foundRace = raceService.findByCircuit(circuit);
        List<Race> raceList = new ArrayList<>();
        raceList.add(race);
        Assert.assertEquals(raceList,foundRace);
    }

    @Test
    public void findRaceByDriver_findsRace(){
        List<Race> foundRace = raceService.findByDriver(driver);
        List<Race> raceList = new ArrayList<>();
        raceList.add(race);
        Assert.assertEquals(raceList,foundRace);
    }

    @Test
    public void getDrivers_returnsDriver(){
        List<User> drivers = raceService.getDrivers(race);
        List<User> expected = new ArrayList<>();
        expected.add(driver);
        Assert.assertEquals(expected,drivers);
    }

    @Test
    public void updateRaceName_ValuesChange(){
        race.setName("24 of LeMans 2022");
        raceService.update(race);
        Race findRace = raceService.find(1);
        Assert.assertEquals("24 of LeMans 2022",findRace.getName());
    }

    @Test
    public void removeRace_NotFound(){
        raceService.remove(race);
        Race findRace = raceService.find(1);
        Assert.assertEquals(null,findRace);
    }
}
