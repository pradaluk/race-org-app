package cz.cvut.kbss.ear.race.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RACE_TEAM")
@NamedQueries({
        @NamedQuery(name = "Team.findMyTeam", query = "SELECT t FROM Team t join TeamMembership tm  on tm.team=t where tm.user=:user " +
                "and tm.membershipState=cz.cvut.kbss.ear.race.model.TeamMembershipState.ACTIVE")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Team extends AbstractEntity{

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String name;

    public User getTeamOwner() {
        return teamOwner;
    }

    public void setTeamOwner(User teamOwner) {
        this.teamOwner = teamOwner;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner", referencedColumnName = "USER_ID")
    private User teamOwner;


    public List<TeamMembership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<TeamMembership> memberships) {
        this.memberships = memberships;
    }

    public List<JoinRequest> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests(List<JoinRequest> joinRequests) {
        this.joinRequests = joinRequests;
    }

    public void addMembership(TeamMembership membership) {
        this.memberships.add(membership);
    }

    public void addJoinRequest(JoinRequest joinRequest) {
        this.joinRequests.add(joinRequest);
    }
    @OneToMany(mappedBy = "team", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @OrderBy("team ASC")
    private List<TeamMembership> memberships = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE)
    @OrderBy("dateTimeCreated DESC")
    private List<JoinRequest> joinRequests = new ArrayList<>();

    public Team(User owner) {
        this.teamOwner = owner;
    }

    public Team() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "name: " + name +
                ", drivers:" + "}";
    }
}
