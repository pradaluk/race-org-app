package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.RacingOrgApp;
import cz.cvut.kbss.ear.race.config.PersistenceConfig;
import cz.cvut.kbss.ear.race.config.ServiceConfig;
import cz.cvut.kbss.ear.race.model.Car;
import cz.cvut.kbss.ear.race.model.CarClass;
import cz.cvut.kbss.ear.race.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

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
public class CarServiceTest extends BaseServiceTestRunner{

    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;

//    @Test
//    public void saveCarFromInvalidParamsDoesntSaveCar() {
//        System.out.println(sut.findAllManufacturers());
//        assertThrows(ValidationException.class,()->{sut.saveFromParams("Ford","Model",2004);});
//        assertThrows(ValidationException.class,()->{sut.saveFromParams("Man","Focus",2004);});
//        assertThrows(ConstraintViolationException.class,()->{sut.saveFromParams("Ford","Focus",1700);});
//    }

    Car audi = new Car();

    @Before
    public void saveCar(){
        audi.setId(1);
        audi.setMaker("Audi");
        audi.setModel("LMS");
        audi.setCarClass(CarClass.GT3);
        carService.persist(audi);
    }

    @Test
    public void NewCar_FoundInDatabase(){

        Car findCar = carService.find(1);
        assertEquals(audi,findCar);

    }
    @Test
    public void RemoveCar_NotFoundInDatabase(){
        Car findCar = carService.find(1);
        assertEquals(audi,findCar);
        carService.remove(audi);
        findCar = carService.find(1);
        assertEquals(null, findCar);
    }
    @Test
    public void UpdateCar_CarUpdated(){
        Car findCar = carService.find(1);
        assertEquals(audi,findCar);
        audi.setModel("LMS EVO");
        carService.update(audi);
        findCar = carService.find(1);
        assertEquals(audi, findCar);
    }
    @Test
    public void findCarByUser(){
        User user = new User();
        user.setPassword("test");
        user.setUsername("test");
        user.setEmail("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setAge(20);
        audi.setOwner(user);
        userService.persist(user);

        carService.persist(audi);
        List<Car> findCars = carService.findByUser(user);
        List<Car> audiList = new ArrayList<Car>();
        audiList.add(audi);
        assertEquals(audiList,findCars);
    }
}
