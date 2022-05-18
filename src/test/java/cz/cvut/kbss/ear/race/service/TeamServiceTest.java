package cz.cvut.kbss.ear.race.service;

import cz.cvut.kbss.ear.race.RacingOrgApp;
import cz.cvut.kbss.ear.race.config.PersistenceConfig;
import cz.cvut.kbss.ear.race.config.ServiceConfig;
import cz.cvut.kbss.ear.race.model.Team;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class, PersistenceConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional(transactionManager = "transactionManager")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan(basePackageClasses = RacingOrgApp.class)
public class TeamServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    Team team = new Team();

    User owner = new User();

    @Before
    public void setUp(){
        owner.setPassword("test");
        owner.setUsername("test");
        owner.setEmail("test");
        owner.setFirstName("test");
        owner.setLastName("test");
        owner.setAge(20);
        owner.setId(2);
        userService.persist(owner);

        team.setTeamOwner(owner);
        team.setName("ERT Racing");
        team.setId(1);
        teamService.create(team,owner);
    }

    @Test
    public void findTeam_foundInDatabase(){
        Team found = teamService.find(1);
        Assert.assertEquals(team,found);
    }

    @Test
    public void findMyTeam_foundInDatabase(){
        Team found = teamService.findMyTeam(owner);
        Assert.assertEquals(team,found);
    }

    @Test
    public void updateTeam_ValuesChange(){
        team.setName("ERT Racing TEAM #29");
        teamService.update(team);
        Team found = teamService.find(1);
        Assert.assertEquals("ERT Racing TEAM #29",found.getName());
    }

    @Test
    public void removeTeam_NotFoundInDatabase(){
        teamService.remove(team);
        Team found = teamService.find(1);
        Assert.assertEquals(null,found);
    }
}
