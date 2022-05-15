package cz.cvut.kbss.ear.race.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CHAMPIONSHIP")
public class Championship extends AbstractEntity{

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany
    @OrderBy("race_date")
    @JoinColumn(name = "championship_id")
    private List<Race> races;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }

    public void addRace(Race race){
        races.add(race);
    }

    public void removeRace(Race race){
        races.remove(race);
    }

}
