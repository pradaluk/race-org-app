package cz.cvut.kbss.ear.race.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "joinrequest")
@NamedQueries({
        @NamedQuery(name = "JoinRequest.findByUser", query = "SELECT j from JoinRequest j WHERE j.user = :user"), })
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class JoinRequest {
    @Column
    private String message;

    @Enumerated(EnumType.STRING)
    private JoinRequestStatus status;

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, updatable = false)
    private Date dateTimeCreated;

    @Column
    private Date dateTimeResolved;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User user;

    public JoinRequest() {
        this.status = JoinRequestStatus.PENDING;
        this.dateTimeCreated = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Date dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public Date getDateTimeResolved() {
        return dateTimeResolved;
    }

    public void setDateTimeResolved(Date dateTimeResolved) {
        this.dateTimeResolved = dateTimeResolved;
    }

    public JoinRequestStatus getStatus() {
        return status;
    }

    public void setStatus(JoinRequestStatus status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
