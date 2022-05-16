package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.RacingOrgApp;
import cz.cvut.kbss.ear.race.config.PersistenceConfig;
import cz.cvut.kbss.ear.race.config.ServiceConfig;
import cz.cvut.kbss.ear.race.model.Car;
import cz.cvut.kbss.ear.race.model.CarClass;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, PersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional(transactionManager = "transactionManager")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackageClasses = RacingOrgApp.class)
public class CarServiceTest extends BaseServiceTestRunner{


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private CarService carService;

//    @Test
//    public void saveCarFromInvalidParamsDoesntSaveCar() {
//        System.out.println(sut.findAllManufacturers());
//        assertThrows(ValidationException.class,()->{sut.saveFromParams("Ford","Model",2004);});
//        assertThrows(ValidationException.class,()->{sut.saveFromParams("Man","Focus",2004);});
//        assertThrows(ConstraintViolationException.class,()->{sut.saveFromParams("Ford","Focus",1700);});
//    }
    @Test
    public void NewCarDoesntThrowException(){
        Car audi = new Car();
        audi.setId(1);
        audi.setMaker("Audi");
        audi.setModel("LMS");
        audi.setCarClass(CarClass.GT3);
        em.persist(audi);

        Car findCar = carService.find(1);
        assertEquals(audi,findCar);
        //assertThrows(NullPointerException.class,()->{carService.persist(audi);});
    }
}
