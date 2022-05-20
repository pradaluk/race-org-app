package cz.cvut.kbss.ear.race.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "RACE_CIRCUIT")
@NamedQueries({
        @NamedQuery(name = "Circuit.findByCountry", query = "SELECT c from Circuit c WHERE c.location = :location"),
        @NamedQuery(name = "Circuit.findByName", query = "SELECT c from Circuit c WHERE c.name = :name")
})
public class Circuit extends AbstractEntity{

    @NotBlank
    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Basic(optional = false)
    @Column(nullable = false)
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
