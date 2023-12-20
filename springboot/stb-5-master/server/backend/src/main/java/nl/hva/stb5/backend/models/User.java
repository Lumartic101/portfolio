package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import nl.hva.stb5.backend.models.joinTables.UserQuestionnaire;
import nl.hva.stb5.backend.views.UserView;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "User")
@DynamicUpdate
//@Loader(namedQuery = "getUsersWithEducationAndFaculty")
//@NamedNativeQuery(name = "getUsersWithEducationAndFaculty",
//query = "select u.id, u.first_name, u.last_name, u.role, u.email, u.password, e.name, f.name, u.education_id FROM user as u inner join education as e on u.education_id = e.id INNER JOIN faculty as f ON e.faculty_id = f.id"
//, resultClass = User.class)

@NamedQueries({
        @NamedQuery(name = "get_all_users_with_education_and_faculty",
                query = "SELECT u FROM User u"),
        @NamedQuery(name = "findUserByEmail",
                query = "SELECT u FROM User u WHERE u.email = ?1")
})
public class User {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({UserView.base.class, UserView.UserEducationFaculty.class})
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    @JsonView({UserView.base.class, UserView.UserEducationFaculty.class})
    private String firstName;

    @Column(name = "last_name")
    @JsonView({UserView.base.class, UserView.UserEducationFaculty.class})
    private String lastName;

    @Column(name = "password")
    @JsonView({UserView.base.class, UserView.UserEducationFaculty.class})
    @JsonIgnore
    private String password;

    @NotNull(message = "{email.required}")

    @Column(name = "email", unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$",
            message = "{invalid.email}")
    @JsonView({UserView.base.class, UserView.UserEducationFaculty.class})
    private String email;

    @Column(name = "role")
    @JsonView({UserView.base.class, UserView.UserEducationFaculty.class})
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(targetEntity = Education.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "education_id", insertable = false, updatable = false)
    @JsonView({UserView.base.class,UserView.UserEducationFaculty.class})
    private Education education;

    @Column(name = "education_id")
    @JsonView({UserView.base.class, UserView.UserEducationFaculty.class})
    private int education_id;

    @OneToMany(mappedBy="user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    //@JsonManagedReference(value = "user_answer")
    private Set<Answer> answers;

    @OneToMany(mappedBy = "userFromJoinTable", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserQuestionnaire> questionnaires;

    public User(int id, String firstName, String lastName, String password, String email, Role role, Education education, Set<Answer> answers, Set<UserQuestionnaire> questionnaires) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.education = education;
        this.answers = answers;
        this.questionnaires = questionnaires;
    }

    public User() {

    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public boolean validateEncodedPassword(String givenPassword) {
        return password.equals(givenPassword);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public int getEducation_id() {
        return education_id;
    }

    public void setEducation_id(int education_id) {
        this.education_id = education_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getEmail(), user.getEmail()) && getRole() == user.getRole() && Objects.equals(getEducation(), user.getEducation()) && Objects.equals(answers, user.answers) && Objects.equals(questionnaires, user.questionnaires);
    }


}
