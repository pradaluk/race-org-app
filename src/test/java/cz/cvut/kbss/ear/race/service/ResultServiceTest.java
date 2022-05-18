package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.RacingOrgApp;
import cz.cvut.kbss.ear.race.config.PersistenceConfig;
import cz.cvut.kbss.ear.race.config.ServiceConfig;
import cz.cvut.kbss.ear.race.model.CarClass;
import cz.cvut.kbss.ear.race.model.Race;
import cz.cvut.kbss.ear.race.model.Result;
import cz.cvut.kbss.ear.race.model.User;
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
public class ResultServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RaceService raceService;

    @Autowired
    private ResultService resultService;

    User user = new User();

    Race race = new Race();

    Result result = new Result();

    @Before
    public void setUp(){
        user.setPassword("test");
        user.setUsername("test");
        user.setEmail("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setAge(20);
        userService.persist(user);

        race.setName("24H of Le Mans");
        race.setRace_date(new Date());
        List<CarClass> classes = new ArrayList<>();
        classes.add(CarClass.GT3);
        race.setClasses(classes);
        race.setEor_date(new Date());
        race.setMaxDrivers(20);
        race.setId(1);
        raceService.create(race);

        result.setUser(user);
        result.setRace(race);
        race.addResult(result);
        resultService.persist(result);
    }

    @Test
    public void findByDriverAndRace_foundInDatabase(){
        Result found = resultService.findByDriverAndRace(user,race);
        Assert.assertEquals(result,found);
    }

    @Test
    public void findByDriver_foundInDatabase(){
        List<Result> found = resultService.findByDriver(user);
        List<Result> expectedList = new ArrayList<>();
        expectedList.add(result);
        Assert.assertEquals(expectedList,found);
    }

    @Test
    public void update_ValuesChange(){
        result.setStartPos(1);
        result.setFinishPos(5);
        resultService.update(result);
        Result found = resultService.findByDriverAndRace(user,race);
        Assert.assertEquals(1,found.getStartPos());
        Assert.assertEquals(5,found.getFinishPos());
    }
}
