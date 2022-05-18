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
import org.springframework.dao.EmptyResultDataAccessException;
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
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @Autowired
    private RaceService raceService;
    @Autowired
    private ResultService resultService;

    Race race = new Race();
    User user = new User();
    Car car = new Car();

    @Before
    public void setUp(){
        car.setId(1);
        car.setMaker("Audi");
        car.setModel("LMS");
        car.setCarClass(CarClass.GT3);
        carService.persist(car);

        user.setPassword("test");
        user.setUsername("test");
        user.setEmail("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setAge(20);
        race.setOrganizor(user);
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
    }

    @Test
    public void findUser_foundInDatabase(){
        User found = userService.find(user.getId());
        Assert.assertEquals(user,found);
    }
    @Test
    public void findByUsername_foundInDatabase(){
        User found = userService.findByUsername("test");
        Assert.assertEquals(user,found);
    }

    @Test
    public void registerForRace_foundResult(){
        userService.registerForRace(race,user,car);
        Result res = resultService.findByDriverAndRace(user,race);
        Assert.assertNotNull(res);
    }

    @Test
    public void withdrawFromRace_NoResultFound(){
        userService.withdrawFromRace(race,user);
        Assert.assertThrows(EmptyResultDataAccessException.class,()->{resultService.findByDriverAndRace(user,race);});
    }

}
