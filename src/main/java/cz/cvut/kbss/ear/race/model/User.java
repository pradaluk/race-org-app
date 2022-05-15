package cz.cvut.kbss.ear.race.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "APP_USER")
@NamedQueries({
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class User extends AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Integer id;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {this.id = id;}

    @Basic(optional = false)
    @Column(nullable = false)
    private String firstName;

    @Basic(optional = false)
    @Column(nullable = false)
    private String lastName;

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String username;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer age;

    @Basic(optional = false)
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Result> results_user;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private TeamMembership membership;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<JoinRequest> joinRequests = new ArrayList<>();

    public User() {
        this.role = Role.GUEST;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(password);
    }

    public void erasePassword() {
        this.password = null;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean isDriver() {
        return role == Role.DRIVER;
    }

    public boolean isOrganizor() {
        return role == Role.ORGANIZER;
    }

    public boolean isGuest() {
        return role == Role.GUEST;
    }
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Result> getResults_user() {
        return results_user;
    }

    public void setResults_user(List<Result> results_user) {
        this.results_user = results_user;
    }
    public TeamMembership getMembership() {
        return membership;
    }

    public void setMembership(TeamMembership membership) {
        this.membership = membership;
    }

    public List<JoinRequest> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests(List<JoinRequest> joinRequests) {
        this.joinRequests = joinRequests;
    }

    @Override
    public String toString() {
        return "User{" +
                firstName + " " + lastName +
                "(" + username + ")}";
    }
}
