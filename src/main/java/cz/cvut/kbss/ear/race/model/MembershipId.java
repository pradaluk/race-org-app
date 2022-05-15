package cz.cvut.kbss.ear.race.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MembershipId implements Serializable {

    @Column(nullable = false, updatable = false)
    private Integer membership_teamid;

    @Column( nullable = false, updatable = false)
    private Integer membership_userid;


    public MembershipId() {}

    public MembershipId(Integer membership_projectid, Integer membership_userid) {
        this.membership_teamid = membership_projectid;
        this.membership_userid = membership_userid;
    }

    public Integer getMembership_projectid() {
        return membership_teamid;
    }

    public void setMembership_projectid(Integer projectId) {
        this.membership_teamid = projectId;
    }

    public Integer getMembership_userid() {
        return membership_userid;
    }

    public void setMembership_userid(Integer userId) {
        this.membership_userid = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembershipId that = (MembershipId) o;
        return membership_teamid.equals(that.membership_teamid) &&
                membership_userid.equals(that.membership_userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(membership_teamid, membership_userid);
    }
}
