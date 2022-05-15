package cz.cvut.kbss.ear.race.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "RESULT")
@NamedQueries({
        @NamedQuery(name = "Result.findByDriverAndRace", query = "SELECT r FROM Result r WHERE r.user = :driver AND r.race = :race"),
        @NamedQuery(name = "Result.findByDriver", query = "SELECT r FROM Result r WHERE r.user = :driver")
})
public class Result {

    @EmbeddedId
    private ResultKey id;

    @ManyToOne
    @MapsId("driverId")
    @JoinColumn(name = "driver_id")
    private User user;

    @ManyToOne
    @MapsId("raceId")
    @JoinColumn(name = "race_id")
    private Race race;

    @Basic(optional = true)
    @Column(nullable = true)
    private int startPos;

    @Basic(optional = true)
    @Column(nullable = true)
    private int finishPos;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    public Result(User driver, Race race, Car car){
        this.user = driver;
        this.race = race;
        this.car = car;
    }

    public Result() {
    }

    @Id
    @GeneratedValue
    @Column(name = "RESULT_ID")
    public ResultKey getId() {
        return id;
    }

    public void setId(ResultKey id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "CAR_ID")
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "RACE_ID")
    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }


    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getFinishPos() {
        return finishPos;
    }

    public void setFinishPos(int finishPos) {
        this.finishPos = finishPos;
    }

}
