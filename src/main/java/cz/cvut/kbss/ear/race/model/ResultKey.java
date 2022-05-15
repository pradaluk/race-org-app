package cz.cvut.kbss.ear.race.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
class ResultKey implements Serializable {

    @Column(name = "driver_id")
    Integer driverId;

    @Column(name = "race_id")
    Integer raceId;

    // standard constructors, getters, and setters
    // hashcode and equals implementation
}