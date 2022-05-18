package cz.cvut.kbss.ear.race.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "RACE")
@NamedQueries({
        @NamedQuery(name = "Race.findByCircuit", query = "SELECT r from Race r WHERE r.circuit = :circuit"),
        @NamedQuery(name = "Race.getDrivers", query = "SELECT u from User u join Result res on u = res.user where res.race = :race"),
        @NamedQuery(name = "Race.getByDriver", query = "SELECT r from Race r join Result res on r = res.race where res.user = :user")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Race extends AbstractEntity {

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    @Column(name = "RACE_ID")
    private Integer id;

    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @Basic(optional = false)
    @Column(nullable = false, columnDefinition = "date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date race_date;

    @Basic(optional = false)
    @Column(nullable = false, columnDefinition = "date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date eor_date;



    @Basic(optional = false)
    @Column(nullable = false)
    private Integer maxDrivers;

    @JsonIgnore
    @OneToMany(mappedBy="race", cascade = CascadeType.REMOVE)
    private List<Result> results_race;

    public List<CarClass> getClasses() {
        return classes;
    }

    public void setClasses(List<CarClass> classes) {
        this.classes = classes;
    }

    @ElementCollection(targetClass = CarClass.class)
    @CollectionTable(name="race_classes")
    @Column(name = "classes", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<CarClass> classes;

    @ManyToOne
    private User organizor;

    @ManyToOne
    private Circuit circuit;

    public Race(){
    }

    public Race(User organizor, Date race_date, String name, Circuit circuit){
        this.race_date = race_date;
        this.organizor = organizor;
        this.name = name;
        this.circuit = circuit;
    }

    public void addResult(Result res){
        if(results_race == null){
            results_race = new ArrayList<Result>();
            results_race.add(res);
            return;
        }
        Objects.requireNonNull(res);
        results_race.add(res);
    }

    public void removeResult(Result res){
        Objects.requireNonNull(res);
        if(results_race == null){
            return;
        }
        results_race.remove(res);
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getRace_date() {
        return race_date;
    }

    public void setRace_date(Date race_date) {
        this.race_date = race_date;
    }

    public List<Result> getResults() {
        return results_race;
    }

    public Integer getMaxDrivers() {
        return maxDrivers;
    }
    public void setResults(List<Result> drivers) {
        this.results_race = drivers;
    }

    public User getOrganizor() {
        return organizor;
    }

    public void setOrganizor(User organizor) {
        this.organizor = organizor;
    }

    public Date getEor_date() {
        return eor_date;
    }

    public void setEor_date(Date eor_date) {
        this.eor_date = eor_date;
    }

    public void setMaxDrivers(Integer maxDrivers) {
        this.maxDrivers = maxDrivers;
    }

    public List<Result> getResults_race() {
        return results_race;
    }

    public void setResults_race(List<Result> results_race) {
        this.results_race = results_race;
    }
}
