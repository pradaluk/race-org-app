package cz.cvut.kbss.ear.race.dao;

import cz.cvut.kbss.ear.race.RacingOrgApp;
import cz.cvut.kbss.ear.race.environment.Generator;
import cz.cvut.kbss.ear.race.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

// For explanatory comments, see ProductDaoTest
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = RacingOrgApp.class)
public class UserDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserDao sut;

    @Test
    public void findByUsernameReturnsPersonWithMatchingUsername() {
        final User user = Generator.generateUser();
        em.persist(user);

        final User result = sut.findByUsername(user.getUsername());
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }

    @Test
    public void findByUsernameReturnsNullForUnknownUsername() {
        assertNull(sut.findByUsername("unknownUsername"));
    }
}
