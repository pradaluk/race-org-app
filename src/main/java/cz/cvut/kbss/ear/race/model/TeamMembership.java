package cz.cvut.kbss.ear.race.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "membership")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "membershipId")
public class TeamMembership implements Serializable {

    @EmbeddedId
    private MembershipId membershipId;

    @ManyToOne(optional = false)
    @MapsId("membership_teamid")
    private Team team;

    @OneToOne(optional = false)
    @MapsId("membership_userid")
    private User user;

    @Enumerated(EnumType.STRING)
    private TeamMembershipState membershipState;

    @Enumerated(EnumType.STRING)
    private TeamRole role;

    public TeamMembership() {
    }

    public TeamMembership(User user, Team team, TeamRole role) {
        this.user = user;
        this.team = team;
        this.role = role;
        this.membershipState = TeamMembershipState.ACTIVE;
    }

    public Team getTeam() {
        return team;
    }

    public void setProject(Team team) {
        this.team = team;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TeamMembershipState getStatus() {
        return membershipState;
    }

    public void setStatus(TeamMembershipState membershipState) {
        this.membershipState = membershipState;
    }

    public MembershipId getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(MembershipId membershipId) {
        this.membershipId = membershipId;
    }

}