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

import java.security.acl.Owner;
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
public class MembershipServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamMembershipService teamMembershipService;

    Team team = new Team();

    User owner = new User();

    User member = new User();

    MembershipId membershipIdOwner = new MembershipId(1, 1);

    MembershipId membershipIdMember = new MembershipId(1, 2);
    @Before
    public void setUp(){
        owner.setPassword("test");
        owner.setUsername("test");
        owner.setEmail("test");
        owner.setFirstName("test");
        owner.setLastName("test");
        owner.setAge(20);
        owner.setId(1);
        userService.persist(owner);

        member.setPassword("test");
        member.setUsername("member");
        member.setEmail("test");
        member.setFirstName("test");
        member.setLastName("test");
        member.setAge(20);
        member.setId(2);
        userService.persist(member);

        team.setTeamOwner(owner);
        team.setName("ERT Racing");
        team.setId(1);
        teamService.create(team,owner);
    }

    @Test
    public void createMembership_foundInDatabase(){
        TeamMembership found = teamMembershipService.find(membershipIdOwner);
        Assert.assertEquals(found.getTeam(), team);
        Assert.assertEquals(found.getUser(), owner);
    }

    @Test
    public void removeUser_MembershipEXPIRED(){
        teamMembershipService.createMembership(team,member,TeamRole.MEMBER);
        TeamMembership found = teamMembershipService.find(membershipIdMember);
        Assert.assertEquals(found.getUser(), member);
        Assert.assertEquals(found.getTeam(), team);

        teamMembershipService.removeUserFromProject(member,team);
        found = teamMembershipService.find(membershipIdMember);
        Assert.assertEquals(TeamMembershipState.EXPIRED, found.getStatus());
    }

}
